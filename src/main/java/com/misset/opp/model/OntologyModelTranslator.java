package com.misset.opp.model;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Service
public final class OntologyModelTranslator {

    private static final Logger logger = Logger.getInstance(OntologyModelTranslator.class);
    // contains information from the SHACL model about the cardinality of the predicates
    private final HashMap<OntClass, List<Property>> required = new HashMap<>();
    private final HashMap<OntClass, List<Property>> singles = new HashMap<>();
    private final HashMap<OntClass, List<Property>> multiple = new HashMap<>();
    private OntologyModel ontologyModel;
    private OntologyModelCache modelCache;
    private OntModel shaclModel;

    public static OntologyModelTranslator getInstance(@NotNull Project project) {
        return project.getService(OntologyModelTranslator.class);
    }

    void loadSimpleModel(OntologyModel model,
                         OntologyModelCache ontologyModelCache,
                         OntModel ontologyModel, OntModel shaclModel) {
        flush();
        this.ontologyModel = model;
        this.modelCache = ontologyModelCache;
        this.shaclModel = shaclModel;
        listShaclClasses().forEach(ontClass -> loadSimpleModelClass(ontologyModel, ontClass));
        listShaclIndividuals(ontologyModel).forEach(individual -> loadSimpleModelIndividual(ontologyModel, individual));
        listGraphshapes().forEach(resource -> loadGraphShapes(ontologyModel, resource));
    }

    private void flush() {
        required.clear();
        singles.clear();
        multiple.clear();
    }

    private Set<OntClass> listShaclClasses() {
        return shaclModel
                .listSubjectsWithProperty(OntologyModelConstants.getRdfType(), OntologyModelConstants.getOwlClass())
                .mapWith(resource -> shaclModel.createClass(resource.getURI()))
                .toSet();
    }

    private Set<Individual> listShaclIndividuals(OntModel ontologyModel) {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate()
                        .equals(OntologyModelConstants.getRdfType()) && ontologyModel.getOntClass(statement.getObject().asResource().getURI()) != null)
                .mapWith(statement -> shaclModel.getIndividual(statement.getSubject().getURI()))
                .filterKeep(OntResource::isIndividual)
                .toSet();
    }

    private Set<Resource> listGraphshapes() {
        return shaclModel
                .listStatements()
                .filterKeep(statement -> statement.getPredicate().equals(OntologyModelConstants.getRdfType()) && statement.getObject()
                        .equals(OntologyModelConstants.getGraphShape()))
                .mapWith(Statement::getSubject)
                .toSet();
    }

    private void loadSimpleModelClass(OntModel ontModel, OntClass ontClass) {
        // create a simple class instance and inherit the superclass(es)
        final OntClass simpleModelClass = ontologyModel.createClass(ontClass.getURI(), ontModel);
        // create one individual per class, this is used as a mock when traversing the paths
        // and discriminate between classes and instances of the class being visited.
        final List<Statement> superClasses = ontClass.listProperties(OntologyModelConstants.getRdfsSubclassOf()).toList();
        if (superClasses.isEmpty()) {
            // base class in the model, subclass of Owl:Thing
            simpleModelClass.addSuperClass(OntologyModelConstants.getOwlThingClass());
        } else {
            superClasses.forEach(statement -> simpleModelClass.addSuperClass(statement.getObject().asResource()));
        }

        // translate the SHACL PATH properties into simple predicate-object statements for this class
        ontClass.listProperties(OntologyModelConstants.getShaclProperty())
                .mapWith(Statement::getObject)
                .mapWith(RDFNode::asResource)
                .filterKeep(resource -> resource.getProperty(OntologyModelConstants.getRdfType()).getObject().equals(OntologyModelConstants.getShaclProperyshape()))
                .forEach(shaclPropertyShape -> getSimpleResourceStatement(ontModel, simpleModelClass, shaclPropertyShape));

        modelCache.cacheClass(simpleModelClass);

        ontologyModel.createIndividual(simpleModelClass, simpleModelClass.getURI() + "_INSTANCE");
    }

    private void loadSimpleModelIndividual(OntModel ontModel, Individual individual) {
        try {
            if (ontModel.getOntResource(individual.getURI()) == null) {
                ontologyModel.createIndividual(individual.getOntClass(), individual.getURI());
                ontModel.add(individual.listProperties());
            }
        } catch (ConversionException conversionException) {
            // do nothing, there might be an input issue in the ontology or some other reason
            // why the provided individual cannot be recreated as an individual in the simple model
            // in any case, this should just be a warning
            logger.warn("Could not create an individual for: " + individual.getURI());
        }
    }

    private void loadGraphShapes(OntModel ontModel, Resource resource) {
        if (ontModel.getOntResource(resource.getURI()) == null) {
            ontologyModel.createIndividual(OntologyModelConstants.getGraphShape(), resource.getURI());
        }
    }

    private void getSimpleResourceStatement(OntModel ontologyModel,
                                            OntClass subject,
                                            Resource shaclPropertyShape) {
        if (!shaclPropertyShape.hasProperty(OntologyModelConstants.getShaclPath())) {
            return;
        }

        // the predicate is extracted from the SHACL PATH and translated into a model property
        final Property predicate = ontologyModel.createProperty(shaclPropertyShape.getProperty(OntologyModelConstants.getShaclPath())
                .getObject()
                .asResource()
                .getURI());

        // the object is extracted from either the SHACL_CLASS or SHACLE_DATATYPE and added as node
        final RDFNode object = getObjectDefinition(shaclPropertyShape);
        if (!(object instanceof Resource)) {
            return;
        }
        subject.addProperty(predicate, object);
        modelCache.cacheSubjectPredicateObject(subject, predicate, object.asResource());

        // cardinality:
        int min = getShaclPropertyInteger(shaclPropertyShape, OntologyModelConstants.getShaclMincount());
        int max = getShaclPropertyInteger(shaclPropertyShape, OntologyModelConstants.getShaclMaxcount());
        if (min == 1) {
            addToMapCollection(required, subject, predicate);
        }
        if (max == 1) {
            addToMapCollection(singles, subject, predicate);
        } else {
            addToMapCollection(multiple, subject, predicate);
        }
    }

    private RDFNode getObjectDefinition(Resource shaclPropertyShape) {
        if (shaclPropertyShape.hasProperty(OntologyModelConstants.getShaclClass())) {
            return shaclPropertyShape.getProperty(OntologyModelConstants.getShaclClass()).getObject();
        } else if (shaclPropertyShape.hasProperty(OntologyModelConstants.getShaclDatatype())) {
            return shaclPropertyShape.getProperty(OntologyModelConstants.getShaclDatatype()).getObject();
        }
        return null;
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

    public OntModel getShaclModel() {
        return shaclModel;
    }

    public boolean isMultiple(Set<OntResource> resources,
                              Property property) {
        return resources.stream().anyMatch(resource -> isMultiple(resource, property));
    }

    public boolean isMultiple(OntResource resource,
                              Property property) {
        return isCardinality(multiple, ontologyModel.toClass(resource), property);
    }

    public boolean isSingleton(Set<OntResource> resources,
                               Property property) {
        return resources.stream().anyMatch(resource -> isSingleton(resource, property));
    }

    public boolean isSingleton(OntResource resource,
                               Property property) {
        return isCardinality(singles, ontologyModel.toClass(resource), property);
    }

    public boolean isRequired(Set<OntResource> resources,
                              Property property) {
        return resources.stream().anyMatch(resource -> isRequired(resource, property));
    }

    public boolean isRequired(OntResource resource,
                              Property property) {
        return isCardinality(required, ontologyModel.toClass(resource), property);
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
}
