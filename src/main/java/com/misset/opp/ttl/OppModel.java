package com.misset.opp.ttl;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wrapper for OntModel (Apache Jena) which has methods to quickly resolve the SHACL implementation of the OPP ontology
 * The wrapper works with 2 implementations of the Ontology Resource: OntClass and Individual
 * <p>
 * When a query resolves a step using the rdf:type, the outcome will be OntClass
 * When a query resolves a step using the ^rdf:type, the outcome will be Individual
 * <p>
 * Only individuals are actually processed for data, meaning that any traversion that is performed on a class
 * must be a valid class predicate such as subClassOf.
 * When parameter types are provided for methods, the class type describes the type of individuals that will be passed into the method argument.
 */
public class OppModel {
    private static final String XSD = "http://www.w3.org/2001/XMLSchema#";

    protected Property SHACL_PATH, SHACL_CLASS, SHACL_DATATYPE, SHACL_PROPERTY, SHACL_PROPERYSHAPE;
    protected Property RDFS_SUBCLASS_OF;
    protected Property RDF_TYPE;
    private List<Property> classModelProperties;

    protected OntResource OWL_CLASS, OWL_THING;
    public OntResource XSD_BOOLEAN, XSD_STRING, XSD_NUMBER;

    public static OppModel INSTANCE;
    private final OntModel shaclModel;
    /**
     * The simple model contains a simple representation of the SHACL based model that is initially loaded
     */
    private final OntModel model;

    public OppModel(OntModel shaclModel) {
        this.shaclModel = shaclModel;
        // the OWL_DL restricts types, a resource can only be either a class, a property or an instance, not multiple at the same time
        // the RDFS_INF inferencing provides the support for sub/superclass logic
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
        setProperties();
        setPrimitives();
        loadSimpleModel();
        INSTANCE = this;
    }

    private Set<OntClass> listClasses() {
        return shaclModel
                .listSubjectsWithProperty(RDF_TYPE, OWL_CLASS)
                .mapWith(resource -> shaclModel.createClass(resource.getURI()))
                .toSet();
    }

    public OntModel getShaclModel() {
        return shaclModel;
    }

    public OntModel getModel() {
        return model;
    }

    private void setProperties() {
        SHACL_PATH = model.createProperty("http://www.w3.org/ns/shacl#path");
        SHACL_CLASS = model.createProperty("http://www.w3.org/ns/shacl#class");
        SHACL_DATATYPE = model.createProperty("http://www.w3.org/ns/shacl#datatype");
        SHACL_PROPERTY = model.createProperty("http://www.w3.org/ns/shacl#property");
        SHACL_PROPERYSHAPE = model.createProperty("http://www.w3.org/ns/shacl#PropertyShape");

        RDFS_SUBCLASS_OF = model.createProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");

        RDF_TYPE = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

        OWL_CLASS = model.createClass("http://www.w3.org/2002/07/owl#Class");
        OWL_THING = model.createClass("http://www.w3.org/2002/07/owl#Thing");

        classModelProperties = List.of(RDFS_SUBCLASS_OF, RDF_TYPE);
    }

    private void setPrimitives() {
        XSD_BOOLEAN = model.createOntResource(XSD + "boolean");
        XSD_STRING = model.createOntResource(XSD + "string");
        XSD_NUMBER = model.createOntResource(XSD + "number");
    }

    private void loadSimpleModel() {
        listClasses().forEach(this::loadSimpleModelClass);
    }

    private void loadSimpleModelClass(OntClass ontClass) {
        // create a simple class instance and inherit the superclass(es)
        final OntClass simpleModelClass = model.createClass(ontClass.getURI());
        // create one individual per class, this is used as a mock when traversing the paths
        // and discriminate between classes and instances of the class being visited.
        final List<Statement> superClasses = ontClass.listProperties(RDFS_SUBCLASS_OF).toList();
        if (superClasses.isEmpty()) {
            // base class in the model, subclass of Owl:Thing
            simpleModelClass.addSuperClass(OWL_THING);
        } else {
            superClasses.forEach(statement -> simpleModelClass.addSuperClass(statement.getObject().asResource()));
        }

        // translate the SHACL PATH properties into simple predicate-object statements for this class
        ontClass.listProperties(SHACL_PROPERTY)
                .mapWith(Statement::getObject)
                .mapWith(RDFNode::asResource)
                .filterKeep(resource -> resource.getProperty(RDF_TYPE).getObject().equals(SHACL_PROPERYSHAPE))
                .forEach((shaclPropertyShape) -> getSimpleResourceStatement(simpleModelClass, shaclPropertyShape));

        simpleModelClass.createIndividual();
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
    }

    /**
     * Returns the list with all predicate-objects for the provided subject
     */
    public Set<Statement> listPredicateObjects(OntResource subject) {
        if(subject.isIndividual()) {
            return listPredicateObjectsForIndividual(subject.asIndividual());
        }
        final OntClass ontClass = toClass(subject);
        final HashSet<Statement> hashSet = new HashSet<>(ontClass.listProperties().toSet());
        ontClass.listSuperClasses().forEach(
                superClass -> hashSet.addAll(superClass.listProperties().toSet())
        );
        return hashSet;
    }
    private Set<Statement> listPredicateObjectsForIndividual(Individual individual) {
        /*
            When traversing the model on an individual, all properties of the (super)class of the instance
            are available. The rdf:type is the exception, an instance should not contain the rdf:type property
            of it's class since that would assert that the instance is an owl:class.
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

    public Set<OntResource> listSubjects(Property predicate, Set<OntResource> objects) {
        return objects.stream()
                .map(classObject -> listSubjects(predicate, classObject))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> listSubjects(Property predicate,
                                         OntResource object) {
        return model.listSubjectsWithProperty(predicate, object)
                .mapWith(model::getOntResource)
                .toSet();
    }

    private OntClass toClass(OntResource resource) {
        if (resource.isIndividual()) {
            return resource.asIndividual().getOntClass();
        }
        return model.createClass(resource.getURI());
    }

    public Set<OntResource> listObjects(Set<OntResource> classSubjects,
                                        Property predicate) {
        return classSubjects.stream()
                .map(subject -> listObjects(subject, predicate))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<OntResource> listObjects(OntResource subject,
                                        Property predicate) {
        if (subject.isIndividual()) {
            return listObjectsForIndividual((Individual) subject, predicate);
        }
        return subject.listProperties(predicate)
                .mapWith(Statement::getObject)
                .mapWith(rdfNode -> model.getOntResource(rdfNode.asResource()))
                .toSet();
    }
    private Set<OntResource> listObjectsForIndividual(Individual subject, Property predicate) {
        if(predicate.equals(RDF_TYPE)) {
            // when rdf:type is called on the individual, the actual class is requested
            return subject.listRDFTypes(true).mapWith(model::getOntResource).toSet();
        } else {
            // otherwise, the graph is traversed by class properties but using instances of those classes
            return subject
                    .listOntClasses(true)
                    .mapWith(ontClass -> ontClass.listProperties(predicate).toList())
                    .filterDrop(List::isEmpty)
                    .toList() // collected all statements for all (super)classes of this instance
                    .stream()
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
    private Set<OntResource> toIndividual(OntResource resource) {
        if(resource.isClass()) {
            return resource.asClass().listInstances().mapWith(OntResource.class::cast).toSet();
        }
        // if the resource is not a class it is most likely a data-type, which we can return as-is
        return Collections.singleton(resource);
    }

    public Set<Resource> listPredicates(List<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listPredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Property> listPredicates(OntResource classSubject) {
        return listPredicateObjects(classSubject)
                .stream()
                .map(Statement::getPredicate)
                .collect(Collectors.toSet());
    }

    public OntResource createResource(String uri) {
        return model.createOntResource(uri);
    }

    public Individual createIndividual(Resource resource) {
        return model.createIndividual(resource.getURI() + "_1", createClass(resource));
    }

    public OntClass createClass(Resource resource) {
        return model.createClass(resource.getURI());
    }

    public Property createProperty(Resource resource) {
        return model.createProperty(resource.getURI());
    }

    public Property createProperty(String uri) {
        return model.createProperty(uri);
    }
}
