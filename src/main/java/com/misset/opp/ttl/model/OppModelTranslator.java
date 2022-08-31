package com.misset.opp.ttl.model;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.*;

public class OppModelTranslator {

    private OppModelTranslator() {
        // empty constructor
    }

    private static final Logger logger = Logger.getInstance(OppModelTranslator.class);
    private static OppModel oppmodel;
    private static OppModelCache modelCache;

    // contains information from the SHACL model about the cardinality of the predicates
    private static final HashMap<OntClass, List<Property>> required = new HashMap<>();
    private static final HashMap<OntClass, List<Property>> singles = new HashMap<>();
    private static final HashMap<OntClass, List<Property>> multiple = new HashMap<>();

    private static OntModel shaclModel;

    static void loadSimpleModel(OppModel oppmodel,
                                OppModelCache oppModelCache,
                                OntModel ontologyModel, OntModel shaclModel) {
        flush();
        OppModelTranslator.oppmodel = oppmodel;
        OppModelTranslator.modelCache = oppModelCache;
        OppModelTranslator.shaclModel = shaclModel;
        listShaclClasses().forEach(ontClass -> loadSimpleModelClass(ontologyModel, ontClass));
        listShaclIndividuals(ontologyModel).forEach(individual -> loadSimpleModelIndividual(ontologyModel, individual));
        listGraphshapes().forEach(resource -> loadGraphShapes(ontologyModel, resource));
    }

    private static void flush() {
        required.clear();
        singles.clear();
        multiple.clear();
    }

    private static Set<OntClass> listShaclClasses() {
        return shaclModel
                .listSubjectsWithProperty(OppModelConstants.getRdfType(), OppModelConstants.getOwlClass())
                .mapWith(resource -> shaclModel.createClass(resource.getURI()))
                .toSet();
    }

    private static Set<Individual> listShaclIndividuals(OntModel ontologyModel) {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate()
                        .equals(OppModelConstants.getRdfType()) && ontologyModel.getOntClass(statement.getObject().asResource().getURI()) != null)
                .mapWith(statement -> shaclModel.getIndividual(statement.getSubject().getURI()))
                .filterKeep(OntResource::isIndividual)
                .toSet();
    }

    private static Set<Resource> listGraphshapes() {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate().equals(OppModelConstants.getRdfType()) && statement.getObject()
                        .equals(OppModelConstants.getGraphShape()))
                .mapWith(Statement::getSubject)
                .toSet();
    }

    private static void loadSimpleModelClass(OntModel ontologyModel, OntClass ontClass) {
        // create a simple class instance and inherit the superclass(es)
        final OntClass simpleModelClass = oppmodel.createClass(ontClass.getURI(), ontologyModel);
        // create one individual per class, this is used as a mock when traversing the paths
        // and discriminate between classes and instances of the class being visited.
        final List<Statement> superClasses = ontClass.listProperties(OppModelConstants.getRdfsSubclassOf()).toList();
        if (superClasses.isEmpty()) {
            // base class in the model, subclass of Owl:Thing
            simpleModelClass.addSuperClass(OppModelConstants.getOwlThingClass());
        } else {
            superClasses.forEach(statement -> simpleModelClass.addSuperClass(statement.getObject().asResource()));
        }

        // translate the SHACL PATH properties into simple predicate-object statements for this class
        ontClass.listProperties(OppModelConstants.getShaclProperty())
                .mapWith(Statement::getObject)
                .mapWith(RDFNode::asResource)
                .filterKeep(resource -> resource.getProperty(OppModelConstants.getRdfType()).getObject().equals(OppModelConstants.getShaclProperyshape()))
                .forEach(shaclPropertyShape -> getSimpleResourceStatement(ontologyModel, simpleModelClass, shaclPropertyShape));

        modelCache.cache(simpleModelClass);

        oppmodel.createIndividual(simpleModelClass, simpleModelClass.getURI() + "_INSTANCE");
    }

    private static void loadSimpleModelIndividual(OntModel ontologyModel, Individual individual) {
        try {
            if (ontologyModel.getOntResource(individual.getURI()) == null) {
                oppmodel.createIndividual(individual.getOntClass(), individual.getURI());
                ontologyModel.add(individual.listProperties());
            }
        } catch (ConversionException conversionException) {
            // do nothing, there might be an input issue in the ontology or some other reason
            // why the provided individual cannot be recreated as an individual in the simple model
            // in any case, this should just be a warning
            logger.warn("Could not create an individual for: " + individual.getURI());
        }
    }

    private static void loadGraphShapes(OntModel ontologyModel, Resource resource) {
        if (ontologyModel.getOntResource(resource.getURI()) == null) {
            oppmodel.createIndividual(OppModelConstants.getGraphShape(), resource.getURI());
        }
    }

    private static void getSimpleResourceStatement(OntModel ontologyModel,
                                                   OntClass subject,
                                                   Resource shaclPropertyShape) {
        if (!shaclPropertyShape.hasProperty(OppModelConstants.getShaclPath())) {
            return;
        }

        // the predicate is extracted from the SHACL PATH and translated into a model property
        final Property predicate = ontologyModel.createProperty(shaclPropertyShape.getProperty(OppModelConstants.getShaclPath())
                .getObject()
                .asResource()
                .getURI());

        // the object is extracted from either the SHACL_CLASS or SHACLE_DATATYPE and added as node
        final RDFNode object = getObjectDefinition(shaclPropertyShape);
        if (!(object instanceof Resource)) {
            return;
        }
        subject.addProperty(predicate, object);
        modelCache.cache(subject, predicate, object.asResource());

        // cardinality:
        int min = getShaclPropertyInteger(shaclPropertyShape, OppModelConstants.getShaclMincount());
        int max = getShaclPropertyInteger(shaclPropertyShape, OppModelConstants.getShaclMaxcount());
        if (min == 1) {
            addToMapCollection(required, subject, predicate);
        }
        if (max == 1) {
            addToMapCollection(singles, subject, predicate);
        } else {
            addToMapCollection(multiple, subject, predicate);
        }
    }

    private static RDFNode getObjectDefinition(Resource shaclPropertyShape) {
        if (shaclPropertyShape.hasProperty(OppModelConstants.getShaclClass())) {
            return shaclPropertyShape.getProperty(OppModelConstants.getShaclClass()).getObject();
        } else if (shaclPropertyShape.hasProperty(OppModelConstants.getShaclDatatype())) {
            return shaclPropertyShape.getProperty(OppModelConstants.getShaclDatatype()).getObject();
        }
        return null;
    }

    private static void addToMapCollection(HashMap<OntClass, List<Property>> map,
                                           OntClass subject,
                                           Property property) {
        final List<Property> propertyList = map.getOrDefault(subject, new ArrayList<>());
        propertyList.add(property);
        map.put(subject, propertyList);
    }

    private static int getShaclPropertyInteger(Resource shape,
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

    public static OntModel getShaclModel() {
        return shaclModel;
    }

    public static boolean isMultiple(Set<OntResource> resources,
                                     Property property) {
        return resources.stream().anyMatch(resource -> isMultiple(resource, property));
    }

    public static boolean isMultiple(OntResource resource,
                                     Property property) {
        return isCardinality(multiple, oppmodel.toClass(resource), property);
    }

    public static boolean isSingleton(Set<OntResource> resources,
                                      Property property) {
        return resources.stream().anyMatch(resource -> isSingleton(resource, property));
    }

    public static boolean isSingleton(OntResource resource,
                                      Property property) {
        return isCardinality(singles, oppmodel.toClass(resource), property);
    }

    public static boolean isRequired(Set<OntResource> resources,
                                     Property property) {
        return resources.stream().anyMatch(resource -> isRequired(resource, property));
    }

    public static boolean isRequired(OntResource resource,
                                     Property property) {
        return isCardinality(required, oppmodel.toClass(resource), property);
    }

    private static boolean isCardinality(Map<OntClass, List<Property>> map,
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
}
