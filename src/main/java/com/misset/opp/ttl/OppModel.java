package com.misset.opp.ttl;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SimpleModificationTracker;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Wrapper for OntModel (Apache Jena) which has methods to quickly resolve the SHACL implementation of the OPP ontology
 * The wrapper works with 2 implementations of the Ontology Resource: OntClass and Individual
 * <p>
 * When a query resolves a step using the rdf:type, the outcome will be OntClass
 * When a query resolves a step using the ^rdf:type, the outcome will be Individual
 * <p>
 * <p>
 * Note:
 * While performance testing the plugin, it became apparent that the Apache Jena model is not really optimized for performance.
 * Probably with good reason but there seems to be no basic caching mechanism which is why querying the model with the same input
 * multiple times will always re-run the entire query.
 * Since we have a good handle on when the model is updated we can safely assume that, when there is no change in the TTL model,
 * using the same query multiple times will always return the same result and thus should be cached.
 */
public class OppModel {
    public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
    private static final String SHACL = "http://www.w3.org/ns/shacl#";
    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String OWL = "http://www.w3.org/2002/07/owl#";
    private static final String OPP = "http://ontologie.politie.nl/def/politie#";
    private static final String PLATFORM = "http://ontologie.politie.nl/def/platform#";

    public Property SHACL_PATH, SHACL_CLASS, SHACL_MINCOUNT, SHACL_MAXCOUNT, SHACL_DATATYPE, SHACL_PROPERTY, SHACL_PROPERYSHAPE;
    protected Property RDFS_SUBCLASS_OF;
    public Property RDF_TYPE;
    public List<Property> classModelProperties;

    protected OntClass OWL_CLASS;
    protected OntClass OWL_THING_CLASS;
    public Individual OWL_THING_INSTANCE;
    public OntClass OPP_CLASS;
    public OntClass GRAPH_SHAPE, GRAPH_CLASS, NAMED_GRAPH_CLASS, TRANSIENT_GRAPH_CLASS;
    public Individual IRI, JSON_OBJECT, ERROR, NAMED_GRAPH, TRANSIENT_GRAPH, MEDEWERKER_GRAPH;
    public OntClass XSD_BOOLEAN, XSD_STRING, XSD_NUMBER, XSD_INTEGER, XSD_DECIMAL, XSD_DATE, XSD_DATETIME, XSD_DURATION;
    public Individual XSD_BOOLEAN_INSTANCE, XSD_STRING_INSTANCE, XSD_NUMBER_INSTANCE, XSD_INTEGER_INSTANCE,
            XSD_DECIMAL_INSTANCE, XSD_DATE_INSTANCE, XSD_DATETIME_INSTANCE, XSD_DURATION_INSTANCE;

    private static final Logger LOGGER = Logger.getInstance(OppModel.class);
    /*
        The modification count whenever the model is loaded
        Any RDF resolving cached values should subscribe to this modification tracker to drop their
        results when something is changed in the model
     */
    public static SimpleModificationTracker ONTOLOGY_MODEL_MODIFICATION_TRACKER = new SimpleModificationTracker();

    // create a default empty model to begin with until the DumbService is smart (finished indexing)
    // and the ontology can be loaded using the FileIndex
    public static OppModel INSTANCE = new OppModel(ModelFactory.createOntologyModel());
    private final OntModel shaclModel;

    // some caching to improve performance, flushed when the model is updated
    HashMap<OntResource, HashMap<Property, Set<OntResource>>> listSubjectsCache = new HashMap<>();
    HashMap<OntResource, HashMap<Property, Set<OntResource>>> listObjectsCache = new HashMap<>();
    HashMap<OntResource, Set<Property>> listPredicatesCache = new HashMap<>();
    HashMap<OntResource, Set<Statement>> listPredicateObjectsCache = new HashMap<>();
    HashMap<String, Set<Individual>> classIndividualsCache = new HashMap<>();
    HashMap<String, OntResource> mappedResources = new HashMap<>();
    HashMap<String, Individual> mappedIndividuals = new HashMap<>();

    // contain information from the SHACL model about the cardinality of the predicates
    HashMap<OntClass, List<Property>> required = new HashMap<>();
    HashMap<OntClass, List<Property>> singles = new HashMap<>();
    HashMap<OntClass, List<Property>> multiple = new HashMap<>();

    Logger logger = Logger.getInstance(OppModel.class);

    /**
     * The simple model contains a simple representation of the SHACL based model that is initially loaded
     */
    private final OntModel model;

    private boolean isUpdating;

    public boolean isUpdating() {
        return isUpdating;
    }

    public OppModel(OntModel shaclModel) {
        this.shaclModel = shaclModel;
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);

        LoggerUtil.runWithLogger(LOGGER, "Loading OppModel from Shacl", () -> {
            isUpdating = true;
            clearCardinality();
            // the OWL_DL restricts types, a resource can only be either a class, a property or an instance, not multiple at the same time
            // the RDFS_INF inferencing provides the support for sub/superclass logic
            setProperties();
            setPrimitives();
            loadSimpleModel();
            incrementModificationCount();
            INSTANCE = this;
            isUpdating = false;
        });

    }

    private void incrementModificationCount() {
        ONTOLOGY_MODEL_MODIFICATION_TRACKER.incModificationCount();
        listSubjectsCache.clear();
        listObjectsCache.clear();
        listPredicatesCache.clear();
        classIndividualsCache.clear();
        listPredicateObjectsCache.clear();
        mappedResources.clear();
        mappedIndividuals.clear();
    }

    private void clearCardinality() {
        required.clear();
        singles.clear();
        multiple.clear();
    }

    private Set<OntClass> listShaclClasses() {
        return shaclModel
                .listSubjectsWithProperty(RDF_TYPE, OWL_CLASS)
                .mapWith(resource -> shaclModel.createClass(resource.getURI()))
                .toSet();
    }

    private Set<Individual> listShaclIndividuals() {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate()
                        .equals(RDF_TYPE) && model.getOntClass(statement.getObject().asResource().getURI()) != null)
                .mapWith(statement -> shaclModel.getIndividual(statement.getSubject().getURI()))
                .filterKeep(OntResource::isIndividual)
                .toSet();
    }

    private Set<Resource> listGraphshapes() {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate().equals(RDF_TYPE) && statement.getObject()
                        .equals(GRAPH_SHAPE))
                .mapWith(Statement::getSubject)
                .toSet();
    }

    protected OntModel getShaclModel() {
        return shaclModel;
    }

    public OntModel getModel() {
        return model;
    }

    private void setProperties() {
        SHACL_PATH = model.createProperty(SHACL + "path");
        SHACL_CLASS = model.createProperty(SHACL + "class");
        SHACL_DATATYPE = model.createProperty(SHACL + "datatype");
        SHACL_MINCOUNT = model.createProperty(SHACL + "minCount");
        SHACL_MAXCOUNT = model.createProperty(SHACL + "maxCount");
        SHACL_PROPERTY = model.createProperty(SHACL + "property");
        SHACL_PROPERYSHAPE = model.createProperty(SHACL + "PropertyShape");

        RDFS_SUBCLASS_OF = model.createProperty(RDFS + "subClassOf");

        RDF_TYPE = model.createProperty(RDF + "type");

        OWL_CLASS = model.createClass(OWL + "Class");
        OWL_THING_CLASS = model.createClass(OWL + "Thing");
        OWL_THING_INSTANCE = OWL_THING_CLASS.createIndividual(OWL + "Thing_INSTANCE");

        OPP_CLASS = model.createClass(OPP + "Class");
        JSON_OBJECT = OPP_CLASS.createIndividual(OPP + "JSON_OBJECT");
        IRI = OPP_CLASS.createIndividual(OPP + "IRI");
        ERROR = OPP_CLASS.createIndividual(OPP + "ERROR");

        GRAPH_CLASS = model.createClass(PLATFORM + "Graph");
        NAMED_GRAPH_CLASS = model.createClass(PLATFORM + "NamedGraph");
        GRAPH_SHAPE = model.createClass(PLATFORM + "GraphShape");
        TRANSIENT_GRAPH_CLASS = model.createClass("http://ontologie.politie.nl/internal/transient#TransientNamedGraph");
        NAMED_GRAPH = NAMED_GRAPH_CLASS.createIndividual(NAMED_GRAPH_CLASS.getURI() + "_INSTANCE");
        MEDEWERKER_GRAPH = NAMED_GRAPH_CLASS.createIndividual(NAMED_GRAPH_CLASS.getURI() + "_MEDEWERKERGRAPH");
        TRANSIENT_GRAPH = TRANSIENT_GRAPH_CLASS.createIndividual(TRANSIENT_GRAPH_CLASS.getURI() + "_INSTANCE");

        classModelProperties = List.of(RDFS_SUBCLASS_OF, RDF_TYPE);
    }

    private void setPrimitives() {
        XSD_BOOLEAN = model.createClass(XSD + "boolean");
        XSD_BOOLEAN_INSTANCE = model.createIndividual(XSD_BOOLEAN);
        XSD_STRING = model.createClass(XSD + "string");
        XSD_STRING_INSTANCE = model.createIndividual(XSD_STRING);
        XSD_NUMBER = model.createClass(XSD + "number");
        XSD_NUMBER_INSTANCE = model.createIndividual(XSD_NUMBER);
        XSD_DECIMAL = model.createClass(XSD + "decimal");
        XSD_DECIMAL.addSuperClass(XSD_NUMBER);
        XSD_DECIMAL_INSTANCE = model.createIndividual(XSD_DECIMAL);
        XSD_INTEGER = model.createClass(XSD + "integer");
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        XSD_INTEGER.addSuperClass(XSD_DECIMAL);
        XSD_INTEGER_INSTANCE = model.createIndividual(XSD_INTEGER);
        XSD_DATETIME = model.createClass(XSD + "dateTime");
        XSD_DATETIME_INSTANCE = model.createIndividual(XSD_DATETIME);
        XSD_DATE = model.createClass(XSD + "date");
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        XSD_DATE.addSuperClass(XSD_DATETIME);
        XSD_DATE_INSTANCE = model.createIndividual(XSD_DATE);

        XSD_DURATION = model.createClass(XSD + "duration");
        XSD_DURATION_INSTANCE = model.createIndividual(XSD_DURATION);
    }

    private void loadSimpleModel() {
        listShaclClasses().forEach(this::loadSimpleModelClass);
        listShaclIndividuals().forEach(this::loadSimpleModelIndividual);
        listGraphshapes().forEach(this::loadGraphShapes);
    }

    private void loadSimpleModelClass(OntClass ontClass) {
        // create a simple class instance and inherit the superclass(es)
        final OntClass simpleModelClass = model.createClass(ontClass.getURI());
        // create one individual per class, this is used as a mock when traversing the paths
        // and discriminate between classes and instances of the class being visited.
        final List<Statement> superClasses = ontClass.listProperties(RDFS_SUBCLASS_OF).toList();
        if (superClasses.isEmpty()) {
            // base class in the model, subclass of Owl:Thing
            simpleModelClass.addSuperClass(OWL_THING_CLASS);
        } else {
            superClasses.forEach(statement -> simpleModelClass.addSuperClass(statement.getObject().asResource()));
        }

        // translate the SHACL PATH properties into simple predicate-object statements for this class
        ontClass.listProperties(SHACL_PROPERTY)
                .mapWith(Statement::getObject)
                .mapWith(RDFNode::asResource)
                .filterKeep(resource -> resource.getProperty(RDF_TYPE).getObject().equals(SHACL_PROPERYSHAPE))
                .forEach((shaclPropertyShape) -> getSimpleResourceStatement(simpleModelClass, shaclPropertyShape));

        simpleModelClass.createIndividual(simpleModelClass.getURI() + "_INSTANCE");
    }

    private void loadSimpleModelIndividual(Individual individual) {
        try {
            if (model.getOntResource(individual.getURI()) == null) {
                model.createIndividual(individual.getURI(), individual.getOntClass());
                model.add(individual.listProperties());
            }
        } catch (ConversionException conversionException) {
            // do nothing, there might be an input issue in the ontology or some other reason
            // why the provided individual cannot be recreated as an individual in the simple model
            // in any case, this should just be a warning
            logger.warn("Could not create an individual for: " + individual.getURI());
        }
    }

    private void loadGraphShapes(Resource resource) {
        if (model.getOntResource(resource.getURI()) == null) {
            model.createIndividual(resource.getURI(), GRAPH_SHAPE);
            model.add(resource.listProperties());
        }
    }

    private void getSimpleResourceStatement(OntClass subject,
                                            Resource shaclPropertyShape) {
        if (!shaclPropertyShape.hasProperty(SHACL_PATH)) {
            return;
        }

        // the predicate is extracted from the SHACL PATH and translated into a model property
        final Property predicate = model.createProperty(shaclPropertyShape.getProperty(SHACL_PATH)
                .getObject()
                .asResource()
                .getURI());

        // the object is extracted from either the SHACL_CLASS or SHACLE_DATATYPE and added as node
        final RDFNode object;
        if (shaclPropertyShape.hasProperty(SHACL_CLASS)) {
            object = shaclPropertyShape.getProperty(SHACL_CLASS).getObject();
        } else if (shaclPropertyShape.hasProperty(SHACL_DATATYPE)) {
            object = shaclPropertyShape.getProperty(SHACL_DATATYPE).getObject();
        } else {
            return;
        }
        subject.addProperty(predicate, object);

        // cardinality:
        int min = getShaclPropertyInteger(shaclPropertyShape, SHACL_MINCOUNT);
        int max = getShaclPropertyInteger(shaclPropertyShape, SHACL_MINCOUNT);
        if (min == 1) {
            addToMapCollection(required, subject, predicate);
        }
        if (max == 1) {
            addToMapCollection(singles, subject, predicate);
        } else {
            addToMapCollection(multiple, subject, predicate);
        }
    }

    private void addToMapCollection(HashMap<OntClass, List<Property>> map,
                                    OntClass subject,
                                    Property property) {
        final List<Property> propertyList = map.getOrDefault(subject, new ArrayList<>());
        propertyList.add(property);
        map.put(subject, propertyList);
    }

    private int getShaclPropertyInteger(Resource shape,
                                        Property property) {
        if (shape.hasProperty(property)) {
            try {
                // catch any wrong usage of the model, not our job to fix that
                return shape.getProperty(property).getObject().asLiteral().getInt();
            } catch (Exception ignored) {
                return 0;
            }
        }
        return 0;
    }

    public Set<OntClass> listClasses() {
        return model.listClasses().toSet();
    }

    /**
     * Returns the list with all predicate-objects for the provided subject
     */
    public Set<Statement> listPredicateObjects(OntResource subject) {
        return LoggerUtil.computeWithLogger(LOGGER, "listPredicateObjects for " + subject.getURI(), () -> {
            if (!listPredicateObjectsCache.containsKey(subject)) {
                final HashSet<Statement> hashSet;
                if (subject.isIndividual()) {
                    hashSet = new HashSet<>(listPredicateObjectsForIndividual(subject.asIndividual()));
                } else if (!subject.isClass()) {
                    hashSet = new HashSet<>();
                } else {
                    OntClass ontClass = subject.asClass();
                    hashSet = new HashSet<>(ontClass.listProperties().toSet());
                    ontClass.listSuperClasses().forEach(
                            superClass -> hashSet.addAll(superClass.listProperties().toSet())
                    );
                }
                listPredicateObjectsCache.put(subject, hashSet);
                return hashSet;
            }
            return listPredicateObjectsCache.get(subject);
        });
    }

    private Set<Statement> listPredicateObjectsForIndividual(Individual individual) {
        /*
            When traversing the model on an individual, all properties of the (super)class of the instance
            are available. The rdf:type is the exception, an instance should not contain the rdf:type property
            of it's class since that result in the instance being all superclass types also. While this is
            semantically correct, it's not how the rdf:type call in the ODT should work.
            In ODT the rdf:type works like getting the direct type.
         */
        final Set<Statement> statements = listPredicateObjectsForClass(individual.getOntClass())
                .stream()
                .filter(statement -> !classModelProperties.contains(statement.getPredicate()))
                .collect(Collectors.toSet());
        statements.addAll(individual.listProperties().toSet());
        return statements;
    }

    private Set<Statement> listPredicateObjectsForClass(OntClass ontClass) {
        final HashSet<Statement> hashSet = new HashSet<>(ontClass.listProperties().toSet());
        ontClass.listSuperClasses().forEach(
                superClass -> hashSet.addAll(superClass.listProperties().toSet())
        );
        return hashSet;
    }

    public Set<OntResource> listSubjects(Property predicate,
                                         Set<OntResource> objects) {
        return LoggerUtil.computeWithLogger(LOGGER, "listSubjects for predicate " + predicate.getURI(), () -> {
            if (objects.contains(OWL_THING_INSTANCE)) {
            /*
                 This is the equivalent of using an Object class.
                 Anything can be an Object so there is no way to resolve
                 to anything in particular
             */
                return Set.of(OWL_THING_INSTANCE);
            }
            return objects.stream()
                    .map(classObject -> listSubjects(predicate, classObject))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        });
    }

    public Set<OntResource> listSubjects(Property predicate,
                                         OntResource object) {
        return LoggerUtil.computeWithLogger(LOGGER, "listSubjects for predicate " + predicate.getURI() + " object " + object.getURI(), () -> {
            final Set<OntResource> cachedResources = listSubjectsCache.getOrDefault(object, new HashMap<>()).get(predicate);
            if (cachedResources == null) {
                final Set<OntResource> resources;
                if (object.isIndividual()) {
                    // the returned subjects should also be the instances of the classes that point to
                    // the ontClass of the Individual
                    resources = listSubjectsForIndividual(predicate, object.asIndividual());
                } else if (object.isClass()) {
                    resources = listSubjectsForClass(predicate, object.asClass());
                } else {
                    resources = Collections.emptySet();
                }
                final HashMap<Property, Set<OntResource>> byObject = listSubjectsCache.getOrDefault(object,
                        new HashMap<>());
                byObject.put(predicate, resources);
                listSubjectsCache.put(object, byObject);
                return resources;
            }
            return cachedResources;
        });
    }

    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates) {
        return LoggerUtil.computeWithLogger(LOGGER, "Filtering " + subjects.size() + " subjects for " + predicates.size() + " predicates", () -> subjects.stream()
                .filter(subject -> hasAnyPredicate(subject, predicates))
                .collect(Collectors.toSet()));
    }

    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates,
                                           Set<OntResource> objects) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "Filtering " + subjects.size() + " subjects for " +
                        predicates.size() + " predicates" +
                        objects.size() + " objects",
                () -> subjects.stream()
                        .filter(subject -> hasAnyPredicateObject(subject, predicates, objects))
                        .collect(Collectors.toSet()));

    }

    public OntClass toClass(OntResource resource) {
        if (resource.isIndividual()) {
            return resource.asIndividual().getOntClass();
        }
        return resource.asClass();
    }

    private boolean hasAnyPredicate(OntResource subject,
                                    Set<OntResource> predicates) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(model::getOntProperty)
                .anyMatch(predicate -> hasPredicate(subject, predicate));
    }

    private boolean hasAnyPredicateObject(OntResource subject,
                                          Set<OntResource> predicates,
                                          Set<OntResource> objects) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(model::getOntProperty)
                .anyMatch(predicate -> hasPredicateObjects(subject, predicate, objects));
    }

    private boolean hasPredicate(OntResource subject,
                                 Property predicate) {
        return toClass(subject).listProperties(predicate).hasNext();
    }

    private boolean hasPredicateObjects(OntResource subject,
                                        Property predicate,
                                        Set<OntResource> objects) {
        return toClass(subject).listProperties(predicate)
                .filterKeep(statement -> objects.stream().anyMatch(statement.getObject()::equals)).hasNext();
    }

    public Set<OntResource> listObjects(Set<OntResource> classSubjects,
                                        Property predicate) {
        if (classSubjects.contains(OWL_THING_INSTANCE)) {
            /*
                 This is the equivalent of using an Object class.
                 Anything can be an Object so there is no way to resolve
                 to anything in particular
             */
            return Set.of(OWL_THING_INSTANCE);
        }
        return classSubjects.stream()
                .map(subject -> listObjects(subject, predicate))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> listObjects(OntResource subject,
                                        Property predicate) {
        final Set<OntResource> cachedResources = listObjectsCache.getOrDefault(subject, new HashMap<>()).get(predicate);
        if (cachedResources == null) {
            final Set<OntResource> resources;
            if (subject.isIndividual()) {
                resources = listObjectsForIndividual(subject.asIndividual(), predicate);
            } else {
                if (!subject.isClass()) {
                    resources = Collections.emptySet(); // no a valid class
                } else {
                    resources = listObjectsForClass(subject.asClass(), predicate);
                }
            }
            final HashMap<Property, Set<OntResource>> bySubject = listObjectsCache.getOrDefault(subject,
                    new HashMap<>());
            bySubject.put(predicate, resources);
            listObjectsCache.put(subject, bySubject);
            return resources;
        }
        return cachedResources;
    }

    private Set<OntResource> listObjectsForIndividual(Individual subject,
                                                      Property predicate) {
        if (predicate.equals(RDF_TYPE)) {
            // when rdf:type is called on the individual, the actual class is requested
            // although this can be resolved via listProperties(predicate), this would also return any of the superclasses
            // and which would lead to [someInstance] / rdf:type / ^rdf:type always returning all classes
            return Set.of(subject.getOntClass());
        } else {
            // an instance has all the properties of it's direct class and all superclasses
            return listOntClasses(subject)
                    .stream()
                    .map(ontClass -> ontClass.listProperties(predicate).toList())
                    .filter(statements -> !statements.isEmpty())
                    .flatMap(Collection::stream)
                    .map(Statement::getObject)
                    .map(RDFNode::asResource)
                    .map(model::getOntResource)
                    // since the subject is an instance, traversing it should also return an instance
                    .map(this::toIndividual)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
    }

    /*
        For some reason, listing the OntClasses via the individual non-direct listOntClasses
        doesn't always provide the entire list of superclasses. The difference might be the
        reasoner using the rdf:type instead of directly accessing known superclasses of the Class
     */
    public Set<OntClass> listOntClasses(OntResource resource) {
        final OntClass ontClass = toClass(resource);
        final HashSet<OntClass> classes = new HashSet<>();
        classes.add(ontClass);
        classes.addAll(ontClass.listSuperClasses().toSet());
        return classes;
    }

    private Set<OntResource> listObjectsForClass(OntClass subject,
                                                 Property predicate) {
        // listing properties on the Class doesn't automatically include the properties
        // on the super-class. This might be an issue with the rdf reasoning (todo)
        final Set<OntResource> objects = subject.listSuperClasses()
                .mapWith(ontClass -> listObjectsForClass(ontClass, predicate))
                .toSet()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        objects.addAll(subject.listProperties(predicate)
                .mapWith(Statement::getObject)
                .mapWith(rdfNode -> model.getOntResource(rdfNode.asResource()))
                .toSet());
        return objects;
    }

    private Set<OntResource> toIndividual(OntResource resource) {
        if (resource.isClass()) {
            return resource.asClass().listInstances().mapWith(OntResource.class::cast).toSet();
        }
        return Collections.singleton(resource);
    }

    public Set<Property> listPredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listPredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Property> listPredicates(OntResource classSubject) {
        final Set<Property> cachedResources = listPredicatesCache.get(classSubject);
        if (cachedResources == null) {
            final Set<Property> predicates = listPredicateObjects(classSubject)
                    .stream()
                    .map(Statement::getPredicate)
                    .collect(Collectors.toSet());
            listPredicatesCache.put(classSubject, predicates);
            return predicates;
        }
        return cachedResources;
    }

    public Set<Property> listReversePredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listReversePredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List any predicate that can point to this subject
     */
    public Set<Property> listReversePredicates(OntResource classSubject) {
        final OntClass subjectAsObject = toClass(classSubject);
        final Set<Property> properties = model.listStatements().toList()
                .stream()
                .filter(statement -> statement.getObject().equals(subjectAsObject))
                .map(Statement::getPredicate)
                .filter(property -> !classModelProperties.contains(property))
                .collect(Collectors.toCollection(HashSet::new));
        if (classSubject.isClass()) {
            properties.addAll(classModelProperties);
        }
        return properties;
    }

    public OntResource getResource(Resource resource) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "Retrieve " + resource.getURI() + " as resource from model",
                () -> model.getOntResource(resource));
    }

    public OntResource getResource(String uri) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "Retrieve " + uri + " as string from model",
                () -> {
                    // Retrieving by string is a rather slow process in Jena,
                    // since it's safe to cache in this case, let's do it!
                    if (mappedResources.containsKey(uri)) {
                        return mappedResources.get(uri);
                    } else {
                        OntResource ontResource = model.getOntResource(uri);
                        mappedResources.put(uri, ontResource);
                        return ontResource;
                    }
                });
    }

    @Nullable
    public Individual getIndividual(@Nullable String uri) {
        return uri == null ? null : LoggerUtil.computeWithLogger(LOGGER,
                "Retrieve " + uri + " as string from model",
                () -> {
                    // Retrieving by string is a rather slow process in Jena,
                    // since it's safe to cache in this case, let's do it!
                    if (mappedIndividuals.containsKey(uri)) {
                        return mappedIndividuals.get(uri);
                    } else {
                        Individual individual = model.getIndividual(uri);
                        mappedIndividuals.put(uri, individual);
                        return individual;
                    }
                });
    }

    public Set<Individual> getClassIndividuals(String classUri) {
        if (classIndividualsCache.containsKey(classUri)) {
            return classIndividualsCache.get(classUri);
        }
        Set<Individual> individuals = calculateClassIndividuals(classUri);
        classIndividualsCache.put(classUri, individuals);
        return individuals;
    }

    private Set<Individual> calculateClassIndividuals(String classUri) {
        if (classUri == null || classUri.equals(OWL_THING_CLASS.getURI())) {
            return Set.of(OWL_THING_INSTANCE);
        }
        return Optional.ofNullable(getClass(classUri))
                .map(ontClass -> ontClass.listInstances(true).toList())
                .stream()
                .flatMap(Collection::stream)
                .filter(OntResource::isIndividual)
                .map(Individual.class::cast)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> toIndividuals(Set<OntResource> resources) {
        return resources.stream().map(
                        resource -> resource.isIndividual() ? Set.of(resource) : OppModel.INSTANCE.getClassIndividuals(resource.getURI())
                ).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> toClasses(Set<OntResource> resources) {
        return resources.stream().map(
                resource -> resource.isIndividual() ? resource.asIndividual().getOntClass(true) : resource
        ).collect(Collectors.toSet());
    }


    public OntClass getClass(Resource resource) {
        return model.getOntClass(resource.getURI());
    }

    @Nullable
    public OntClass getClass(@Nullable String uri) {
        return uri == null ? null : model.getOntClass(uri);
    }

    public Property getProperty(Resource resource) {
        return model.getProperty(resource.getURI());
    }

    @Nullable
    public Property getProperty(@Nullable String uri) {
        return uri == null ? null : model.getProperty(uri);
    }

    private Set<Resource> listSubjectsWithProperty(Property property,
                                                   OntClass ontClass) {
        // Include the superclasses
        // for example, if ClassB has ClassA as superclass and ClassA has propertyA. Then ClassC can point
        // to an instance of ClassB with propertyA also.
        List<OntClass> allClasses = new ArrayList<>(ontClass.listSuperClasses().toList());
        allClasses.add(ontClass);

        return allClasses
                .stream()
                .map(_ontClass -> model.listSubjectsWithProperty(property, _ontClass))
                .map(ExtendedIterator::toSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<OntResource> listSubjectsForClass(Property predicate,
                                                  OntClass object) {
        if (predicate.equals(RDF_TYPE)) {
            // called /^rdf:type on a class, return all instances:
            final Set<? extends OntResource> ontResources = object.listInstances(true).toSet();
            return ontResources
                    .stream()
                    .map(model::getOntResource)
                    .collect(Collectors.toSet());
        }
        return listSubjectsWithProperty(predicate, object)
                .stream()
                .map(model::getOntResource)
                .collect(Collectors.toSet());
    }

    private Set<OntResource> listSubjectsForIndividual(Property predicate,
                                                       Individual object) {
        return listSubjectsWithProperty(predicate, object.getOntClass())
                .stream()
                .map(model::getOntResource)
                .filter(Objects::nonNull)
                .filter(OntResource::isClass)
                .map(OntResource::asClass)
                .map(ontClass -> ontClass.listInstances().toSet())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Method to retrieve an existing Resource from the Ontology
     * If the resource doesn't exist but it can be matched to a known Iri (by RegEx pattern), it will be added
     * as an instance of the specified class.
     * For example, used to register known data graphs
     */
    @Nullable
    public OntResource getOntResource(String uri,
                                      Project project) {
        final OntResource resource = model.getOntResource(uri);
        if (resource != null) {
            return resource;
        }

        // try via known Iris:
        final Map<String, String> modelInstanceMapping = SettingsState.getInstance(project).modelInstanceMapping;
        return modelInstanceMapping.keySet()
                .stream()
                .filter(regEx -> Pattern.compile(regEx).matcher(uri).matches())
                .map(modelInstanceMapping::get)
                .map(model::getOntClass)
                .filter(Objects::nonNull)
                .map(ontClass -> ontClass.createIndividual(uri))
                .peek(individual -> {
                    NotificationGroupManager.getInstance().getNotificationGroup("Update Ontology")
                            .createNotification(
                                    "Added " + individual.getURI() + " as " + individual.getOntClass(true),
                                    NotificationType.INFORMATION)
                            .setIcon(OMTFileType.INSTANCE.getIcon())
                            .notify(project);
                })
                .findFirst()
                .orElse(null);
    }

    public boolean areCompatible(Set<OntResource> resourcesA,
                                 Set<OntResource> resourcesB) {
        return resourcesB.stream()
                .anyMatch(resourceB -> areCompatible(resourcesA, resourceB));
    }

    private boolean areCompatible(Set<OntResource> resourcesA,
                                  OntResource resourceB) {
        return resourcesA.stream()
                .anyMatch(resourceA -> areCompatible(resourceA, resourceB));
    }

    /**
     * Validate that 2 resources are compatible by the criteria that
     * resourceB is compatible to resourceA when resourceB is resourceA or any of the subclasses of resourceA.
     */
    public boolean areCompatible(OntResource resourceA,
                                 OntResource resourceB) {
        if (resourceA.equals(resourceB) ||
                resourceA.equals(OWL_THING_INSTANCE) ||
                resourceB.equals(OWL_THING_INSTANCE)) {
            return true;
        }
        if (resourceA.isIndividual()) {
            // compare individuals by their classes
            if (!resourceB.isIndividual()) {
                return false;
            } else {
                // for 2 individuals, check if classA is part of the classes for B
                return resourceB.asIndividual().listOntClasses(false)
                        .toList()
                        .contains(resourceA.asIndividual().getOntClass());
            }
        }
        if (resourceA.isProperty() || resourceB.isProperty()) {
            // properties are only compatible when equal
            return resourceA.equals(resourceB);
        }
        if (resourceA.isClass()) {
            if (!resourceB.isClass()) {
                return false;
            } else {
                return resourceB.equals(resourceA) ||
                        resourceB.asClass().listSuperClasses().toList().contains(resourceA.asClass());
            }
        }
        return false;
    }

    public boolean isMultiple(Set<OntResource> resources,
                              Property property) {
        return resources.stream().allMatch(resource -> isMultiple(resource, property));
    }

    public boolean isMultiple(OntResource resource,
                              Property property) {
        return isCardinality(multiple, toClass(resource), property);
    }

    public boolean isSingleton(Set<OntResource> resources,
                               Property property) {
        return resources.stream().allMatch(resource -> isSingleton(resource, property));
    }

    public boolean isSingleton(OntResource resource,
                               Property property) {
        return isCardinality(singles, toClass(resource), property);
    }

    public boolean isRequired(Set<OntResource> resources,
                              Property property) {
        return resources.stream().allMatch(resource -> isRequired(resource, property));
    }

    public boolean isRequired(OntResource resource,
                              Property property) {
        return isCardinality(required, toClass(resource), property);
    }

    private boolean isCardinality(Map<OntClass, List<Property>> map,
                                  OntClass ontClass,
                                  Property property) {
        if (ontClass == null) {
            return false;
        }
        if (map.getOrDefault(ontClass, Collections.emptyList()).contains(property)) {
            return true;
        }
        final OntClass superClass = ontClass.getSuperClass();
        if (superClass.equals(ontClass)) {
            return false;
        }
        return isCardinality(map, superClass, property);
    }
}
