package com.misset.opp.ttl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;
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

    public OntClass OWL_CLASS;
    public OntClass OWL_THING_CLASS;
    public Individual OWL_THING_INSTANCE;
    public OntClass OPP_CLASS, JSON, GRAPH_SHAPE, GRAPH_CLASS, NAMED_GRAPH_CLASS, TRANSIENT_GRAPH_CLASS;
    public Individual IRI, JSON_OBJECT, ERROR, NAMED_GRAPH, TRANSIENT_GRAPH, MEDEWERKER_GRAPH, VOID;
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
    HashMap<OntResource, Set<Property>> listReversePredicatesCache = new HashMap<>();
    HashMap<OntResource, Set<Statement>> listPredicateObjectsCache = new HashMap<>();
    HashMap<OntResource, Set<OntClass>> listOntClassesCache = new HashMap<>();
    HashMap<String, Set<OntResource>> toIndividualCache = new HashMap<>();
    HashMap<String, Individual> getIndividualCache = new HashMap<>();
    HashMap<String, OntClass> toClassCache = new HashMap<>();
    HashMap<String, OntClass> getClassCache = new HashMap<>();
    HashMap<OntClass, Set<OntClass>> superClassesCache = new HashMap<>();
    HashMap<String, OntResource> mappedResourcesCache = new HashMap<>();
    HashMap<OntResource, Boolean> isIndividualCache = new HashMap<>();
    HashMap<OntResource, Boolean> isClassCache = new HashMap<>();
    HashMap<OntClass, List<OntClass>> listSubclassesCache = new HashMap<>();
    HashMap<OntClass, List<OntClass>> listSuperclassesCache = new HashMap<>();

    // contain information from the SHACL model about the cardinality of the predicates
    HashMap<OntClass, List<Property>> required = new HashMap<>();
    HashMap<OntClass, List<Property>> singles = new HashMap<>();
    HashMap<OntClass, List<Property>> multiple = new HashMap<>();

    Logger logger = Logger.getInstance(OppModel.class);

    private final ReentrantReadWriteLock lock;

    /**
     * The simple model contains a simple representation of the SHACL based model that is initially loaded
     */
    private OntModel model;

    public OppModel(OntModel shaclModel) {
        this.lock = new ReentrantReadWriteLock();
        this.shaclModel = shaclModel;
        OntModel ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);

        LoggerUtil.runWithLogger(LOGGER, "Loading OppModel from Shacl", () -> runWithWriteLock("Loading OppModel from Shacl", () -> {
            clearCardinality();
            // the OWL_DL restricts types, a resource can only be either a class, a property or an instance, not multiple at the same time
            // the RDFS_INF inferencing provides the support for sub/superclass logic
            setProperties(ontologyModel);
            setPrimitives(ontologyModel);
            incrementModificationCount();
            loadSimpleModel(ontologyModel);
            INSTANCE = this;
            this.model = ontologyModel;
        }));
    }

    private void incrementModificationCount() {
        ONTOLOGY_MODEL_MODIFICATION_TRACKER.incModificationCount();
        listSubjectsCache.clear();
        listObjectsCache.clear();
        listPredicatesCache.clear();
        listReversePredicatesCache.clear();
        listPredicateObjectsCache.clear();
        mappedResourcesCache.clear();
        getIndividualCache.clear();
        getClassCache.clear();
        listOntClassesCache.clear();
        toIndividualCache.clear();
        toClassCache.clear();
        isIndividualCache.clear();
        listSubclassesCache.clear();
        listSuperclassesCache.clear();
    }

    private void clearCardinality() {
        required.clear();
        singles.clear();
        multiple.clear();
    }

    protected OntModel getShaclModel() {
        return shaclModel;
    }

    public OntModel getModel() {
        return model;
    }

    /*
        Prepare Ontology model
     */
    private Set<OntClass> listShaclClasses() {
        return shaclModel
                .listSubjectsWithProperty(RDF_TYPE, OWL_CLASS)
                .mapWith(resource -> shaclModel.createClass(resource.getURI()))
                .toSet();
    }

    private Set<Individual> listShaclIndividuals(OntModel ontologyModel) {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate()
                        .equals(RDF_TYPE) && ontologyModel.getOntClass(statement.getObject().asResource().getURI()) != null)
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

    private void setProperties(OntModel ontologyModel) {
        SHACL_PATH = ontologyModel.createProperty(SHACL + "path");
        SHACL_CLASS = ontologyModel.createProperty(SHACL + "class");
        SHACL_DATATYPE = ontologyModel.createProperty(SHACL + "datatype");
        SHACL_MINCOUNT = ontologyModel.createProperty(SHACL + "minCount");
        SHACL_MAXCOUNT = ontologyModel.createProperty(SHACL + "maxCount");
        SHACL_PROPERTY = ontologyModel.createProperty(SHACL + "property");
        SHACL_PROPERYSHAPE = ontologyModel.createProperty(SHACL + "PropertyShape");

        RDFS_SUBCLASS_OF = ontologyModel.createProperty(RDFS + "subClassOf");
        RDF_TYPE = ontologyModel.createProperty(RDF + "type");

        OWL_CLASS = ontologyModel.createClass(OWL + "Class");
        OWL_THING_CLASS = ontologyModel.createClass(OWL + "Thing");
        OWL_THING_INSTANCE = createIndividual(OWL_THING_CLASS, OWL + "Thing_INSTANCE");

        OPP_CLASS = ontologyModel.createClass(OPP + "Class");
        JSON = ontologyModel.createClass(OPP + "JSON");
        JSON_OBJECT = createIndividual(JSON, OPP + "JSON_OBJECT");
        IRI = createIndividual(OPP_CLASS, OPP + "IRI");
        ERROR = createIndividual(OPP_CLASS, OPP + "ERROR");
        VOID = createIndividual(OPP_CLASS, OPP + "VOID");

        GRAPH_CLASS = ontologyModel.createClass(PLATFORM + "Graph");
        NAMED_GRAPH_CLASS = ontologyModel.createClass(PLATFORM + "NamedGraph");
        GRAPH_SHAPE = ontologyModel.createClass(PLATFORM + "GraphShape");
        TRANSIENT_GRAPH_CLASS = ontologyModel.createClass("http://ontologie.politie.nl/internal/transient#TransientNamedGraph");
        NAMED_GRAPH = createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_INSTANCE");
        MEDEWERKER_GRAPH = createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_MEDEWERKERGRAPH");
        TRANSIENT_GRAPH = createIndividual(TRANSIENT_GRAPH_CLASS, TRANSIENT_GRAPH_CLASS.getURI() + "_INSTANCE");

        classModelProperties = List.of(RDFS_SUBCLASS_OF, RDF_TYPE);
    }

    private void setPrimitives(OntModel ontologyModel) {
        XSD_BOOLEAN = ontologyModel.createClass(XSD + "boolean");
        XSD_BOOLEAN_INSTANCE = createIndividual(XSD_BOOLEAN);
        XSD_STRING = ontologyModel.createClass(XSD + "string");
        XSD_STRING_INSTANCE = createIndividual(XSD_STRING);
        XSD_NUMBER = ontologyModel.createClass(XSD + "number");
        XSD_NUMBER_INSTANCE = createIndividual(XSD_NUMBER);
        XSD_DECIMAL = ontologyModel.createClass(XSD + "decimal");
        XSD_DECIMAL.addSuperClass(XSD_NUMBER);
        XSD_DECIMAL_INSTANCE = createIndividual(XSD_DECIMAL);
        XSD_INTEGER = ontologyModel.createClass(XSD + "integer");
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        XSD_INTEGER.addSuperClass(XSD_DECIMAL);
        XSD_INTEGER_INSTANCE = createIndividual(XSD_INTEGER);
        XSD_DATETIME = ontologyModel.createClass(XSD + "dateTime");
        XSD_DATETIME_INSTANCE = createIndividual(XSD_DATETIME);
        XSD_DATE = ontologyModel.createClass(XSD + "date");
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        XSD_DATE.addSuperClass(XSD_DATETIME);
        XSD_DATE_INSTANCE = createIndividual(XSD_DATE);

        XSD_DURATION = ontologyModel.createClass(XSD + "duration");
        XSD_DURATION_INSTANCE = createIndividual(XSD_DURATION);
    }

    private void loadSimpleModel(OntModel ontologyModel) {
        listShaclClasses().forEach(ontClass -> loadSimpleModelClass(ontologyModel, ontClass));
        listShaclIndividuals(ontologyModel).forEach(individual -> loadSimpleModelIndividual(ontologyModel, individual));
        listGraphshapes().forEach(resource -> loadGraphShapes(ontologyModel, resource));
    }

    private void loadSimpleModelClass(OntModel ontologyModel, OntClass ontClass) {
        // create a simple class instance and inherit the superclass(es)
        final OntClass simpleModelClass = ontologyModel.createClass(ontClass.getURI());
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
                .forEach((shaclPropertyShape) -> getSimpleResourceStatement(ontologyModel, simpleModelClass, shaclPropertyShape));
        toClassCache.put(ontClass.getURI(), simpleModelClass);
        isIndividualCache.put(simpleModelClass, false);
        isClassCache.put(simpleModelClass, true);

        createIndividual(simpleModelClass, simpleModelClass.getURI() + "_INSTANCE");
    }

    private void loadSimpleModelIndividual(OntModel ontologyModel, Individual individual) {
        try {
            if (ontologyModel.getOntResource(individual.getURI()) == null) {
                createIndividual(individual.getOntClass(), individual.getURI());
                ontologyModel.add(individual.listProperties());
            }
        } catch (ConversionException conversionException) {
            // do nothing, there might be an input issue in the ontology or some other reason
            // why the provided individual cannot be recreated as an individual in the simple model
            // in any case, this should just be a warning
            logger.warn("Could not create an individual for: " + individual.getURI());
        }
    }

    private void loadGraphShapes(OntModel ontologyModel, Resource resource) {
        if (ontologyModel.getOntResource(resource.getURI()) == null) {
            createIndividual(GRAPH_SHAPE, resource.getURI());
            ontologyModel.add(resource.listProperties());
        }
    }

    private void getSimpleResourceStatement(OntModel ontologyModel,
                                            OntClass subject,
                                            Resource shaclPropertyShape) {
        if (!shaclPropertyShape.hasProperty(SHACL_PATH)) {
            return;
        }

        // the predicate is extracted from the SHACL PATH and translated into a model property
        final Property predicate = ontologyModel.createProperty(shaclPropertyShape.getProperty(SHACL_PATH)
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
        int max = getShaclPropertyInteger(shaclPropertyShape, SHACL_MAXCOUNT);
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

    /**
     * The Apache Jena Ontology model is not thread-safe when reading and writing at the same time.
     * Therefore, all operations regarding the model should be wrapped in a read lock. This uses
     * a ReentrantReadWriteLock which will allow for any number of consecutive read threads or a single
     * write lock.
     * Make sure to also use this when calling methods directly on ontology members such as OntClass.listIndividuals etc.
     */
    public <T> T computeWithReadLock(String description, Supplier<T> compute) {
        return LoggerUtil.computeWithLogger(LOGGER, description, () -> {
            lock.readLock().lock();
            T result;
            try {
                result = compute.get();
            } finally {
                lock.readLock().unlock();
            }
            return result;
        });
    }

    public void runWithWriteLock(String description, Runnable runnable) {
        LoggerUtil.runWithLogger(LOGGER, description, () -> {
            lock.writeLock().lock();
            try {
                runnable.run();
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    public Set<OntClass> listClasses() {
        return computeWithReadLock("OppModel::listClasses", () -> getModel().listClasses().toSet());
    }

    public Set<OntClass> listXSDTypes() {
        return Set.of(
                XSD_BOOLEAN,
                XSD_STRING,
                XSD_INTEGER,
                XSD_DECIMAL,
                XSD_DATETIME,
                XSD_DATE,
                XSD_DURATION);
    }

    /**
     * Returns the list with all predicate-objects for the provided subject
     */
    public Set<Statement> listPredicateObjects(OntResource subject) {
        return LoggerUtil.computeWithLogger(LOGGER, "listPredicateObjects for " + subject.getURI(), () -> {
            if (!listPredicateObjectsCache.containsKey(subject)) {
                return computeWithReadLock("OppModel::listPredicateObjects", () -> {
                    final HashSet<Statement> hashSet;
                    if (isIndividual(subject)) {
                        hashSet = new HashSet<>(listPredicateObjectsForIndividual(subject.asIndividual()));
                    } else if (!isClass(subject)) {
                        hashSet = new HashSet<>();
                    } else {
                        OntClass ontClass = toClass(subject);
                        hashSet = new HashSet<>(ontClass.listProperties().toSet());
                        ontClass.listSuperClasses().forEach(
                                superClass -> hashSet.addAll(superClass.listProperties().toSet())
                        );
                    }
                    listPredicateObjectsCache.put(subject, hashSet);
                    return hashSet;
                });
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
        return computeWithReadLock("OppModel::listPredicateObjectsForIndividual", () -> {
            final Set<Statement> statements = listPredicateObjectsForClass(individual.getOntClass())
                    .stream()
                    .filter(statement -> !classModelProperties.contains(statement.getPredicate()))
                    .collect(Collectors.toSet());
            statements.addAll(individual.listProperties().toSet());
            return statements;
        });
    }

    private Set<Statement> listPredicateObjectsForClass(OntClass ontClass) {
        return computeWithReadLock("OppModel::listPredicateObjectsForClass", () -> {
            final HashSet<Statement> hashSet = new HashSet<>(ontClass.listProperties().toSet());
            ontClass.listSuperClasses().forEach(
                    superClass -> hashSet.addAll(superClass.listProperties().toSet())
            );
            return hashSet;
        });
    }

    public Set<OntResource> listSubjects(Property predicate,
                                         Set<OntResource> objects) {
        if (objects.contains(OWL_THING_INSTANCE)) {
            return Set.of(OWL_THING_INSTANCE);
        }
        return objects.stream()
                .map(classObject -> listSubjects(predicate, classObject))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the subjects that have the specified predicate-object combination
     */
    public Set<OntResource> listSubjects(Property predicate,
                                         OntResource object) {
        final Set<OntResource> cachedResources = listSubjectsCache.getOrDefault(object, new HashMap<>()).get(predicate);
        if (cachedResources == null) {
            final Set<OntResource> resources;
            if (isIndividual(object)) {
                // the returned subjects should also be the instances of the classes that point to
                // the ontClass of the Individual
                resources = listSubjectsForIndividual(predicate, object.asIndividual());
            } else if (isClass(object)) {
                resources = listSubjectsForClass(predicate, toClass(object));
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
    }

    /**
     * Filter the set of subjects to only ones that contain any of provided predicates
     */
    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates) {
        return subjects.stream()
                .filter(subject -> hasAnyPredicate(subject, predicates))
                .collect(Collectors.toSet());
    }

    private boolean hasAnyPredicate(OntResource subject,
                                    Set<OntResource> predicates) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(getModel()::getOntProperty)
                .anyMatch(predicate -> listPredicates(subject).contains(predicate));
    }

    /**
     * Filter the set of subjects to only ones that contain at least one of the predicates and one of the objects
     */
    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates,
                                           Set<OntResource> objects) {
        return subjects.stream()
                .filter(subject -> hasAnyPredicateObject(subject, predicates, objects))
                .collect(Collectors.toSet());
    }

    private boolean hasAnyPredicateObject(OntResource subject,
                                          Set<OntResource> predicates,
                                          Set<OntResource> objects) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(getModel()::getOntProperty)
                .anyMatch(predicate -> hasPredicateObjects(subject, predicate, objects));
    }

    private boolean hasPredicateObjects(OntResource subject,
                                        Property predicate,
                                        Set<OntResource> objects) {
        HashSet<OntResource> _objects = new HashSet<>(listObjects(subject, predicate));
        _objects.retainAll(objects);
        return !_objects.isEmpty();
    }

    private String getResourceId(OntResource resource) {
        return Optional.ofNullable(resource.getURI()).orElseGet(() -> resource.getId().toString());
    }

    /**
     * Converts the OntResource into an OntClass. If the provided resource is an individual, it will
     * return the ontClass of that individual
     */
    public @Nullable OntClass toClass(OntResource resource) {
        if (resource == null) {
            return null;
        }
        String id = getResourceId(resource);
        if (toClassCache.containsKey(id)) {
            return toClassCache.get(id);
        } else {
            return computeWithReadLock("OppModel::toClass(resource)", () -> {
                final OntClass ontClass;
                if (resource.isIndividual()) {
                    ontClass = resource.asIndividual().listOntClasses(true).next();
                } else if (isPredicate(resource)) {
                    ontClass = null;
                } else {
                    ontClass = resource.asClass();
                }
                toClassCache.put(id, ontClass);
                return ontClass;
            });
        }
    }

    /**
     * Converts the OntResources into OntClasses. If the provided resource is an individual, it will
     * return the ontClass of that individual
     */
    public Set<OntClass> toClasses(Set<OntResource> resources) {
        return resources.stream().map(this::toClass).collect(Collectors.toSet());
    }

    /**
     * Returns the super classes of the provided ontClass, not including itself
     */
    public Set<OntClass> getSuperClasses(OntClass ontClass) {
        if (superClassesCache.containsKey(ontClass)) {
            return superClassesCache.get(ontClass);
        } else {
            return computeWithReadLock("OppModel::getSuperClasses(ontClass)", () -> {
                Set<OntClass> superClasses = ontClass.listSuperClasses().toSet();
                superClassesCache.put(ontClass, superClasses);
                return superClasses;
            });
        }
    }

    /**
     * Returns the objects that the provided subjects link on the provided predicate
     * Different subjects can have different types using the same predicate. All types will be returned
     */
    public Set<OntResource> listObjects(Set<OntResource> classSubjects,
                                        Property predicate) {
        if (classSubjects.contains(OWL_THING_INSTANCE)) {
            return Set.of(OWL_THING_INSTANCE);
        }
        return classSubjects.stream()
                .map(subject -> listObjects(subject, predicate))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<OntResource> listObjects(OntResource subject,
                                         Property predicate) {
        final Set<OntResource> cachedResources = listObjectsCache.getOrDefault(subject, new HashMap<>()).get(predicate);
        if (cachedResources == null) {
            final Set<OntResource> resources;
            if (subject.isIndividual()) {
                // when the subject is an individual, the returned resources are also individuals
                resources = listObjectsForIndividual(subject.asIndividual(), predicate);
            } else {
                if (!subject.isClass()) {
                    resources = Collections.emptySet(); // no a valid class
                } else {
                    resources = listObjectsForClass(toClass(subject), predicate);
                }
            }
            final HashMap<Property, Set<OntResource>> bySubject =
                    listObjectsCache.getOrDefault(subject, new HashMap<>());
            bySubject.put(predicate, resources);
            listObjectsCache.put(subject, bySubject);
            return resources;
        } else {
            return cachedResources;
        }
    }

    private Set<OntResource> listObjectsForIndividual(Individual subject,
                                                      Property predicate) {
        if (subject == null) {
            return null;
        } else if (predicate.equals(RDF_TYPE)) {
            return Optional.ofNullable((OntResource) toClass(subject)).map(Set::of).orElse(Collections.emptySet());
        }
        return computeWithReadLock("OppModel::listObjectsForIndividual - " + subject.getURI(),
                () -> listOntClasses(subject)
                        .stream()
                        .map(ontClass -> ontClass.listProperties(predicate).toList())
                        .filter(statements -> !statements.isEmpty())
                        .flatMap(Collection::stream)
                        .map(Statement::getObject)
                        .map(RDFNode::asResource)
                        .map(getModel()::getOntResource)
                        // since the subject is an instance, traversing it should also return an instance
                        .map(this::toIndividuals)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
    }

    private Set<OntResource> listObjectsForClass(OntClass subject,
                                                 Property predicate) {
        return computeWithReadLock("OppModel::listObjectsForClass", () -> {
            final Set<OntResource> objects = subject.listSuperClasses()
                    .mapWith(ontClass -> listObjectsForClass(ontClass, predicate))
                    .toSet()
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            objects.addAll(subject.listProperties(predicate)
                    .mapWith(Statement::getObject)
                    .mapWith(rdfNode -> getModel().getOntResource(rdfNode.asResource()))
                    .toSet());
            return objects;
        });
    }

    /**
     * Returns all possible subclasses for the provided instances
     */
    public Set<OntResource> appendInstancesWithSubclasses(Set<OntResource> resources) {
        if (resources.stream().noneMatch(OWL_THING_INSTANCE::equals) &&
                resources.stream().allMatch(this::isIndividual)) {
            HashSet<OntResource> subclasses = resources.stream().map(this::toClass)
                    .map(this::listSubclasses)
                    .flatMap(Collection::stream)
                    .map(this::toIndividuals)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toCollection(HashSet::new));
            subclasses.addAll(resources);
            return subclasses;
        } else {
            return resources;
        }
    }

    /**
     * Returns the ontology classes that this resource inherits / implements.
     * For example, ResourceB which is a subclass of ResourceA.
     * <p>
     * If resource is an instance of ResourceA it will return ResourceA
     * If resource is an instance of ResourceB it will return ResourceA and ResourceB
     * If resource is the class ResourceB it will return ResourceA and ResourceB
     */
    public Set<OntClass> listOntClasses(OntResource resource) {
        if (listOntClassesCache.containsKey(resource)) {
            return listOntClassesCache.get(resource);
        } else {
            return Optional.ofNullable(toClass(resource))
                    .map(ontClass -> {
                        final HashSet<OntClass> classes = new HashSet<>();
                        classes.add(ontClass);
                        classes.addAll(listSuperClasses(ontClass));
                        listOntClassesCache.put(resource, classes);
                        return classes;
                    })
                    .orElseGet(HashSet::new);
        }
    }

    /**
     * Converts the provided resource into Individuals
     * If the resource is ClassA, it will return all instances of ClassA
     * If the resource is an instance of Class, it will return itself wrapped in a Set
     */
    public Set<OntResource> toIndividuals(Set<? extends OntResource> resources) {
        return resources.stream()
                .map(this::toIndividuals)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> toIndividuals(OntResource resource) {
        String id = getResourceId(resource);
        if (toIndividualCache.containsKey(id)) {
            return toIndividualCache.get(id);
        } else {
            Set<OntResource> individuals = Collections.emptySet();
            if (resource.equals(OWL_CLASS)) {
                return individuals;
            }
            if (getClass(resource) != null) {
                OntClass ontClass = toClass(resource);
                if (ontClass != null) {
                    individuals =
                            computeWithReadLock("OppModel::toIndividuals(OntResource) - " + id,
                                    () -> ontClass.listInstances(true).mapWith(OntResource.class::cast).toSet());
                }
            } else {
                individuals = Collections.singleton(resource);
            }
            toIndividualCache.put(id, individuals);
            return individuals;
        }
    }

    /**
     * List the predicates available on the provided classes
     */
    public Set<Property> listPredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listPredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the predicates available on the provided classes
     */
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

    /**
     * List the predicates that point to the provided classes
     */
    public Set<Property> listReversePredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listReversePredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the predicates that point to the provided class
     */
    public Set<Property> listReversePredicates(OntResource classSubject) {
        if (listReversePredicatesCache.containsKey(classSubject)) {
            return listReversePredicatesCache.get(classSubject);
        } else {
            return computeWithReadLock("OppModel::listReversePredicates(OntResource)", () -> {
                final OntClass subjectAsObject = toClass(classSubject);
                final Set<Property> properties = getModel().listStatements().toList()
                        .stream()
                        .filter(statement -> statement.getObject().equals(subjectAsObject))
                        .map(Statement::getPredicate)
                        .filter(property -> !classModelProperties.contains(property))
                        .collect(Collectors.toCollection(HashSet::new));
                if (classSubject.isClass()) {
                    properties.addAll(classModelProperties);
                }
                listReversePredicatesCache.put(classSubject, properties);
                return properties;
            });
        }
    }

    @Nullable
    public Individual getIndividual(@Nullable String uri) {
        // Retrieving by string is a rather slow process in Jena,
        // since it's safe to cache in this case, let's do it!
        if (getIndividualCache.containsKey(uri)) {
            return getIndividualCache.get(uri);
        } else {
            return computeWithReadLock("OppModel::getIndividual(String)", () -> {
                Individual individual = getModel().getIndividual(uri);
                getIndividualCache.put(uri, individual);
                return individual;
            });
        }
    }

    /**
     * Returns class individuals, similar to the toIndividuals method, however,
     * it returns OWL_THING_INSTANCE as nullsafe response
     */
    public Set<OntResource> getClassIndividuals(String classUri) {
        if (toIndividualCache.containsKey(classUri)) {
            return toIndividualCache.get(classUri);
        }
        Set<OntResource> individuals = calculateClassIndividuals(classUri);
        toIndividualCache.put(classUri, individuals);
        return individuals;
    }

    private Set<OntResource> calculateClassIndividuals(String classUri) {
        if (classUri == null || classUri.equals(OWL_THING_CLASS.getURI())) {
            return Set.of(OWL_THING_INSTANCE);
        }
        if (classUri.equals(OWL_CLASS.getURI())) {
            return Set.of(OWL_CLASS);
        }
        return Optional.ofNullable(getClass(classUri))
                .map(this::toIndividuals)
                .orElse(Collections.emptySet());
    }

    public OntClass getClass(Resource resource) {
        return getClass(resource.getURI());
    }

    /**
     * Returns the OntClass only if it exists in the model
     * To translate an individual to its corresponding class, use toClass
     */
    @Nullable
    public OntClass getClass(@Nullable String uri) {
        if (uri == null) {
            return null;
        }
        if (!getClassCache.containsKey(uri)) {
            getClassCache.put(uri, getModel().getOntClass(uri));
        }
        return getClassCache.get(uri);
    }

    public Boolean isClass(OntResource resource) {
        if (isClassCache.containsKey(resource)) {
            return isClassCache.get(resource);
        } else if (isIndividualCache.containsKey(resource)) {
            return !isIndividualCache.get(resource);
        }

        boolean b = getClass(resource) != null;
        isClassCache.put(resource, b);
        return b;
    }

    public Boolean isIndividual(OntResource resource) {
        if (isIndividualCache.containsKey(resource)) {
            return isIndividualCache.get(resource);
        } else if (isClassCache.containsKey(resource)) {
            return !isClassCache.get(resource);
        } else {
            Boolean isIndividual = computeWithReadLock("OppModel::isIndividual(OntResource) " + resource, resource::isIndividual);
            isIndividualCache.put(resource, isIndividual);
            return isIndividual;
        }
    }

    public Boolean isPredicate(OntResource resource) {
        return getProperty(resource) != null && !isClass(resource);
    }

    public Property getProperty(Resource resource) {
        String uri = resource.getURI();
        if (uri == null) {
            return null;
        }
        return computeWithReadLock("OppModel::getProperty(Resource) " + resource, () -> getModel().getProperty(uri));
    }

    @Nullable
    public Property getProperty(@Nullable String uri) {
        return computeWithReadLock("OppModel::getProperty(String) " + uri, () -> uri == null ? null : getModel().getProperty(uri));
    }

    private Set<Resource> listSubjectsWithProperty(Property property,
                                                   OntClass ontClass) {
        return computeWithReadLock("OppModel::listSubjectsWithProperty(Property, OntClass)", () -> {
            // Include the superclasses
            // for example, if ClassB has ClassA as superclass and ClassA has propertyA. Then ClassC can point
            // to an instance of ClassB with propertyA also.
            List<OntClass> allClasses = new ArrayList<>(ontClass.listSuperClasses().toList());
            allClasses.add(ontClass);

            return allClasses
                    .stream()
                    .map(_ontClass -> getModel().listSubjectsWithProperty(property, _ontClass))
                    .map(ExtendedIterator::toSet)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        });
    }

    private Set<OntResource> listSubjectsForClass(Property predicate,
                                                  OntClass object) {
        if (predicate.equals(RDF_TYPE)) {
            // called /^rdf:type on a class, return all instances:
            return toIndividuals(object);
        }
        return listSubjectsWithProperty(predicate, object)
                .stream()
                .map(getModel()::getOntResource)
                .collect(Collectors.toSet());
    }

    private Set<OntResource> listSubjectsForIndividual(Property predicate,
                                                       Individual object) {
        OntClass individualClass = toClass(object);
        if (individualClass == null) {
            return Collections.emptySet();
        }
        return listSubjectsWithProperty(predicate, individualClass)
                .stream()
                .map(getModel()::getOntResource)
                .filter(Objects::nonNull)
                .filter(OntResource::isClass)
                .map(OntResource::asClass)
                .map(this::toIndividuals)
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
        if (mappedResourcesCache.containsKey(uri)) {
            return mappedResourcesCache.get(uri);
        } else {
            final OntResource resource = getModel().getOntResource(uri);
            if (resource != null) {
                mappedResourcesCache.put(uri, resource);
                return resource;
            }

            if (project == null) {
                return null;
            }
            final Map<String, String> modelInstanceMapping = SettingsState.getInstance(project).modelInstanceMapping;
            return modelInstanceMapping.keySet()
                    .stream()
                    .filter(regEx -> Pattern.compile(regEx).matcher(uri).matches())
                    .map(modelInstanceMapping::get)
                    .map(getModel()::getOntClass)
                    .filter(Objects::nonNull)
                    .map(ontClass -> addIndividual(ontClass, uri, project))
                    .findFirst()
                    .orElse(null);
        }
    }

    private Individual addIndividual(OntClass ontClass, String uri, Project project) {
        if (mappedResourcesCache.containsKey(uri)) {
            // It's possible that multiple read threads come to the same conclusion
            // to add this Individual as write thread.
            return (Individual) mappedResourcesCache.get(uri);
        }
        final Individual individual = createIndividual(ontClass, uri);
        NotificationGroupManager.getInstance().getNotificationGroup("Update Ontology")
                .createNotification(
                        "Added " + getResourceId(individual) + " as " + OppModel.INSTANCE.toClass(individual),
                        NotificationType.INFORMATION)
                .setIcon(OMTFileType.INSTANCE.getIcon())
                .notify(project);
        mappedResourcesCache.put(uri, individual);

        SettingsState.getInstance(project).knownInstances.put(
                uri, getResourceId(ontClass)
        );
        return individual;
    }

    private Individual createIndividual(OntClass ontClass) {
        return createIndividual(ontClass, null);
    }

    public Individual createIndividual(OntClass ontClass, @Nullable String uri) {
        ontClass = toClassCache.getOrDefault(ontClass.getURI(), ontClass);

        final Individual individual = uri == null ? ontClass.createIndividual() : ontClass.createIndividual(uri);
        String individualURI = getResourceId(individual);
        String ontClassURI = getResourceId(ontClass);

        // add to caches
        Set<OntResource> individuals = toIndividualCache.getOrDefault(ontClassURI, new HashSet<>());
        individuals.add(individual);
        toClassCache.put(individualURI, ontClass);
        toIndividualCache.put(ontClassURI, individuals);
        getIndividualCache.put(individualURI, individual);
        isIndividualCache.put(individual, true);
        isClassCache.put(individual, false);
        return individual;
    }

    /**
     * Validate that 2 resources are compatible by the criteria that
     * resourceB is compatible to resourceA when resourceB is resourceA or any of the subclasses of resourceA.
     */
    public boolean areCompatible(Set<OntResource> resourcesA,
                                 Set<OntResource> resourcesB) {
        return LoggerUtil.computeWithLogger(LOGGER, "Comparing compatibility between " +
                        resourcesA + " and " + resourcesB,
                () -> resourcesB.stream()
                        .anyMatch(resourceB -> areCompatible(resourcesA, resourceB)));

    }

    private boolean areCompatible(Set<OntResource> resourcesA,
                                  OntResource resourceB) {
        return resourcesA.stream()
                .anyMatch(resourceA -> areCompatible(resourceA, resourceB));
    }

    private boolean areCompatible(OntResource resourceA,
                                  OntResource resourceB) {
        if (resourceA.equals(resourceB) ||
                resourceA.equals(OWL_THING_INSTANCE) ||
                resourceB.equals(OWL_THING_INSTANCE)) {
            return true;
        }
        if (isIndividual(resourceA)) {
            // compare individuals by their classes
            if (!isIndividual(resourceB)) {
                return false;
            } else {
                // for 2 individuals, check if classA is part of the classes for B
                return listOntClasses(resourceB)
                        .contains(toClass(resourceA));
            }
        }
        if (isPredicate(resourceA) || isPredicate(resourceB)) {
            // properties are only compatible when equal
            return resourceA.equals(resourceB);
        }
        if (isClass(resourceA)) {
            if (!isClass(resourceB)) {
                return false;
            } else {
                return resourceB.equals(resourceA) ||
                        getSuperClasses(getClass(resourceB)).contains(getClass(resourceA));
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
        if (superClass == null || superClass.equals(ontClass)) {
            return false;
        }
        return isCardinality(map, superClass, property);
    }

    /**
     * Only use this for documentation / completion purposes
     */
    public List<OntClass> listSuperClasses(OntClass ontClass) {
        return listClasses(ontClass, listSuperclassesCache, subclass -> subclass.listSuperClasses(true).toList());
    }

    /**
     * Only use this for documentation / completion purposes
     */
    public List<OntClass> listSubclasses(OntClass ontClass) {
        return listClasses(ontClass, listSubclassesCache, subclass -> subclass.listSubClasses(true).toList());
    }

    private List<OntClass> listClasses(OntClass ontClass,
                                       HashMap<OntClass, List<OntClass>> mapped,
                                       Function<OntClass, List<OntClass>> withMethod) {
        if (mapped.containsKey(ontClass)) {
            return mapped.get(ontClass);
        }

        return computeWithReadLock("OppModel::listClasses(OntClass, HashMap, Function)", () -> {
            ArrayList<OntClass> listClasses = new ArrayList<>();
            listClassesRecursive(ontClass, listClasses, mapped, withMethod);
            mapped.put(ontClass, listClasses);
            return listClasses;
        });
    }

    private void listClassesRecursive(OntClass ontClass,
                                      List<OntClass> listClasses,
                                      HashMap<OntClass, List<OntClass>> mapped,
                                      Function<OntClass, List<OntClass>> withMethod) {
        // first check if the recursion hit an ontClass that was already cached
        if (mapped.containsKey(ontClass)) {
            listClasses.addAll(mapped.get(ontClass));
            return;
        }
        if (ontClass == null) {
            return;
        }

        List<OntClass> ontClasses = withMethod.apply(ontClass);
        for (OntClass subclass : ontClasses) {
            if (!listClasses.contains(subclass)) {
                listClassesRecursive(subclass, listClasses, mapped, withMethod);
            } else if (!subclass.equals(OWL_THING_CLASS)) {
                LOGGER.error("It looks like there is a recursion in the model when processing "
                        + getResourceId(subclass) + " while it was already present in the recursive class collector");
            }
        }
        listClasses.addAll(ontClasses);
    }

    public Collection<? extends OntClass> listSubclasses(Set<OntClass> classes) {
        return classes.stream()
                .map(this::listSubclasses)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the list matched to the type of the template
     * If the list contains ontClass and the template is an Individual, it will return
     * the instances of the classes. If the list contains Individuals and the template
     * is a class it will return the Classes.
     */
    public Set<OntResource> toType(Set<OntResource> resources, OntResource template) {
        if (template instanceof Individual) {
            return toIndividuals(resources);
        } else if (template instanceof OntClass) {
            return toClasses(resources).stream().map(OntResource.class::cast).collect(Collectors.toSet());
        } else {
            return resources;
        }
    }

    public void addFromJson(JsonObject references, ProgressIndicator indicator, boolean referenceDetails) {

        Set<String> strings = references.keySet();
        int total = strings.size();
        int processed = 0;
        indicator.setIndeterminate(false);
        indicator.setFraction(0d);
        for (String classType : strings) {
            indicator.setText(classType);
            OntClass ontClass = getClass(classType);
            if (ontClass == null) {
                return;
            }

            JsonElement jsonElement = references.get(classType);
            if (jsonElement.isJsonArray()) {
                Set<String> individuals = new HashSet<>();
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonArray.forEach(element -> individuals.add(getSubject(element)));
                runWithWriteLock("OppModel::addFromJson", () -> {
                    individuals.forEach(uri -> {
                        Individual individual = createIndividual(ontClass, uri);
                        if (referenceDetails) {
                            addReferenceDetails(jsonArray, individual);
                        }
                    });
                });
            }
            processed++;
            indicator.setFraction((double) processed / total);
        }
    }

    private String getSubject(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        JsonObject jsonStatement = element.getAsJsonObject();
        if (jsonStatement.has("s")) {
            return jsonStatement.get("s").getAsString();
        }
        return null;
    }

    private void addReferenceDetails(JsonArray array, Individual individual) {
        array.forEach(element -> {
            if (element != null && element.isJsonObject()) {
                JsonObject jsonStatement = element.getAsJsonObject();
                if (jsonStatement.has("s") && jsonStatement.has("p") && jsonStatement.has("o")) {
                    String subject = jsonStatement.get("s").getAsString();
                    if (subject.equals(getResourceId(individual))) {
                        String propertyIri = jsonStatement.get("p").getAsString();
                        String object = jsonStatement.get("o").getAsString();
                        Property property = getModel().getProperty(propertyIri);
                        // the individual is already added to a class, no need to add the rdf_type predicate again
                        if (!property.equals(RDF_TYPE)) {
                            individual.addProperty(property, object);
                        }
                    }
                }
            }
        });
    }

}
