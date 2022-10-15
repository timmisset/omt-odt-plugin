package com.misset.opp.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.misset.opp.model.constants.XSD;
import com.misset.opp.testcase.BasicTestCase;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;

class OntologyModelTest extends LightJavaCodeInsightFixtureTestCase {
    public static final String CLASS_A_INSTANCE_A = "http://ontology#ClassA_InstanceA";
    public static final String ONTOLOGY_CLASS_A = "http://ontology#ClassA";
    public static final String ONTOLOGY_CLASS_B = "http://ontology#ClassB";
    public static final String ONTOLOGY_CLASS_BSUB = "http://ontology#ClassBSub";
    private OntClass CLASS_A;
    private OntClass CLASS_B;
    private OntClass CLASS_BSUB;
    private Individual CLASS_A_INDIVIDUAL;
    private Individual CLASS_B_INDIVIDUAL;
    private Individual CLASS_BSUB_INDIVIDUAL;

    private OntologyModel ontologyModel;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        ontologyModel = BasicTestCase.initOntologyModel(getProject());
        CLASS_A = createClass("ClassA");
        CLASS_B = createClass("ClassB");
        CLASS_BSUB = createClass("ClassBSub");
        CLASS_BSUB.setSuperClass(CLASS_B);
        CLASS_A_INDIVIDUAL = ontologyModel.createIndividual(CLASS_A, CLASS_A.getURI() + "_INSTANCE");
        CLASS_B_INDIVIDUAL = ontologyModel.createIndividual(CLASS_B, CLASS_B.getURI() + "_INSTANCE");
        CLASS_BSUB_INDIVIDUAL = ontologyModel.createIndividual(CLASS_BSUB, CLASS_BSUB.getURI() + "_INSTANCE");
    }

    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void listPredicateOnClass() {
        final Set<Property> predicates = ontologyModel.listPredicates(CLASS_A);
        assertTrue(predicates.contains(createProperty("booleanPredicate")));
        assertTrue(predicates.contains(OntologyModelConstants.getRdfType()));
        assertTrue(predicates.contains(OntologyModelConstants.getRdfsSubclassOf()));
    }

    @Test
    void listPredicateOnIndividual() {
        final Set<Property> predicates = ontologyModel.listPredicates(CLASS_A_INDIVIDUAL);
        assertTrue(predicates.contains(createProperty("booleanPredicate")));
        assertTrue(predicates.contains(OntologyModelConstants.getRdfType()));
        assertFalse(predicates.contains(OntologyModelConstants.getRdfsSubclassOf()));
    }

    @Test
    void listPredicatesMultipleClasses() {
        final Set<Property> predicates = ontologyModel.listPredicates(
                Set.of(CLASS_A, createClass("ClassB")));
        assertTrue(predicates.contains(createProperty("booleanPredicate")));
        assertTrue(predicates.contains(createProperty("stringPredicate")));
        assertTrue(predicates.contains(OntologyModelConstants.getRdfType()));
    }

    @Test
    void listPredicatesSubclasses() {
        final Set<Property> predicates = ontologyModel.listPredicates(createClass("ClassBSub"));
        assertTrue(predicates.contains(createProperty("numberPredicate")));
        assertTrue(predicates.contains(createProperty("stringPredicate")));
    }

    @Test
    void testListSubjectsRdfType() {
        final Set<OntResource> resources = ontologyModel.listSubjects(OntologyModelConstants.getRdfType(), Set.of(CLASS_A));
        assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof Individual && ((Individual) resource).hasOntClass(CLASS_A)
        ));
    }

    @Test
    void testListSubjectsResourcePredicate() {
        final Set<OntResource> resources = ontologyModel.listSubjects(createProperty("booleanPredicate"),
                Set.of(createXsdResource("boolean")));
        assertTrue(resources.stream().anyMatch(CLASS_A::equals));
    }

    @Test
    void testListSubjectsClassPredicate() {
        final Set<OntResource> resources = ontologyModel.listSubjects(OntologyModelConstants.getRdfsSubclassOf(),
                Set.of(createResource("ClassB")));
        assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof OntClass && resource.equals(createResource(
                        "ClassBSub"))
        ));
    }

    @Test
    void testListObjectsPredicate() {
        final Set<OntResource> resources = ontologyModel.listObjects(Set.of(CLASS_A_INDIVIDUAL),
                createProperty("booleanPredicate"));
        assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(OntologyModelConstants.getXsdBooleanInstance())
        ));
    }

    @Test
    void testListObjectsInstanceToInstance() {
        final Set<OntResource> resources = ontologyModel.listObjects(Set.of(CLASS_A_INDIVIDUAL),
                createProperty("classPredicate"));
        assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof Individual && ((Individual) resource).hasOntClass(createClass(
                        "ClassBSub"))
        ));
    }

    @Test
    void testListObjectsRdfTypeOnIndividual() {
        final Set<OntResource> resources = ontologyModel.listObjects(Set.of(CLASS_A_INDIVIDUAL), OntologyModelConstants.getRdfType());
        assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(CLASS_A))
        );
    }

    @Test
    void testListObjectsRdfTypeOnClass() {
        final Set<OntResource> resources = ontologyModel.listObjects(Set.of(CLASS_A), OntologyModelConstants.getRdfType());
        assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(OntologyModelConstants.getOwlClass()))
        );
    }

    @Test
    void testInstanceValues() {
        final Individual individual = ontologyModel.getIndividual(CLASS_A_INSTANCE_A);
        final RDFNode booleanPredicate = individual.listPropertyValues(
                ontologyModel.getProperty(createProperty("booleanPredicate"))
        ).next();
        assertTrue(booleanPredicate.isLiteral());
        assertTrue(booleanPredicate.asLiteral().getBoolean());
    }

    @Test
    void testAnOppClassIsAnOwlClass() {
        final OntClass ontClass = ontologyModel.getClass(CLASS_A.getURI());
        assertEquals(ontClass.getRDFType(), OntologyModelConstants.getOwlClass());
    }

    @Test
    void testAnOppClassIsNotASubclassOfAnOwlClass() {
        final Individual individual = ontologyModel.getIndividual(CLASS_A_INSTANCE_A);
        OntClass ontClass = ontologyModel.toClass(individual);
        assertFalse(ontClass.listSuperClasses(false).toList().contains(OntologyModelConstants.getOwlClass()));
    }

    @Test
    void testAnOppClassIsASubclassOfAnOwlThing() {
        final Individual individual = ontologyModel.getIndividual(CLASS_A_INSTANCE_A);
        OntClass ontClass = ontologyModel.toClass(individual);
        assertTrue(ontClass.listSuperClasses(false).toList().contains(OntologyModelConstants.getOwlThingClass()));
    }

    @Test
    void testAreCompatibleTrueWhenEqualIndividuals() {
        assertTrue(areCompatible(CLASS_A_INSTANCE_A, CLASS_A_INSTANCE_A));
    }

    @Test
    void testAreCompatibleTrueInstanceWithOWLThing() {
        assertTrue(areCompatible(OntologyModelConstants.getOwlThingInstance().getURI(), CLASS_A_INSTANCE_A));
        assertTrue(areCompatible(CLASS_A_INSTANCE_A, OntologyModelConstants.getOwlThingInstance().getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceWithOWLClass() {
        assertFalse(areCompatible(OntologyModelConstants.getOwlClass().getURI(), CLASS_A_INSTANCE_A));
        assertFalse(areCompatible(CLASS_A_INSTANCE_A, OntologyModelConstants.getOwlClass().getURI()));
    }

    @Test
    void testAreCompatibleTrueClassWithOWLClass() {
        assertTrue(areCompatible(OntologyModelConstants.getOwlClass().getURI(), CLASS_A.getURI()));
        assertTrue(areCompatible(OntologyModelConstants.getRdfsClass().getURI(), CLASS_A.getURI()));
        assertTrue(areCompatible(OntologyModelConstants.getRdfsResource().getURI(), CLASS_A.getURI()));
    }

    @Test
    void testAreCompatibleFalseOWLClassWithClass() {
        assertFalse(areCompatible(CLASS_A.getURI(), OntologyModelConstants.getOwlClass().getURI()));
    }

    @Test
    void testAreCompatibleFalseClassWithOWLThing() {
        assertFalse(areCompatible(OntologyModelConstants.getOwlThingInstance().getURI(), ONTOLOGY_CLASS_A));
        assertFalse(areCompatible(ONTOLOGY_CLASS_A, OntologyModelConstants.getOwlThingInstance().getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceWithClass() {
        assertFalse(areCompatible(CLASS_A_INSTANCE_A, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseAny2DifferentClasses() {
        assertFalse(areCompatible(ONTOLOGY_CLASS_A, ONTOLOGY_CLASS_B));
        assertFalse(areCompatible(ONTOLOGY_CLASS_B, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseIdenticalClasses() {
        // Classes that are identical are not compatible
        // Classes are hardly ever directly compatible unless the requirement is owl:Class and the provided
        // is an instance of owl:Class which is any class with a rdf:type owl:Class
        assertFalse(areCompatible(ONTOLOGY_CLASS_A, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseClassWithSubclass() {
        assertFalse(areCompatible(ONTOLOGY_CLASS_B, ONTOLOGY_CLASS_BSUB));
        assertFalse(areCompatible(ONTOLOGY_CLASS_BSUB, ONTOLOGY_CLASS_B));
    }

    @Test
    void testAreCompatibleTrueInstanceOfSubclass() {
        assertTrue(areCompatible(CLASS_B_INDIVIDUAL.getURI(), CLASS_BSUB_INDIVIDUAL.getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceOfSuperclass() {
        assertFalse(areCompatible(CLASS_BSUB_INDIVIDUAL.getURI(), CLASS_B_INDIVIDUAL.getURI()));
    }

    @Test
    void testAreCompatibleNumbers() {
        // hierarchy is: integer subclassOf decimal subclassOf number
        assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.DECIMAL_INSTANCE.getUri()));
        assertTrue(areCompatible(XSD.DECIMAL_INSTANCE.getUri(),
                XSD.INTEGER_INSTANCE.getUri()));
        assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.INTEGER_INSTANCE.getUri()));
        assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.NUMBER_INSTANCE.getUri()));

        assertFalse(areCompatible(XSD.DECIMAL_INSTANCE.getUri(),
                XSD.NUMBER_INSTANCE.getUri()));
        assertFalse(areCompatible(XSD.INTEGER_INSTANCE.getUri(),
                XSD.DECIMAL_INSTANCE.getUri()));
    }

    @Test
    void testAreCompatibleDates() {
        // hierarchy is: date subclassOf dateTime
        assertTrue(areCompatible(XSD.DATETIME_INSTANCE.getUri(),
                XSD.DATE_INSTANCE.getUri()));

        assertFalse(areCompatible(XSD.DATE_INSTANCE.getUri(),
                XSD.DATETIME_INSTANCE.getUri()));
    }

    @Test
    void addFromJson() {
        JsonArray array = new JsonArray();
        JsonObject typeTriple = new JsonObject();
        typeTriple.addProperty("s", "http://ontology/ClassA_InstanceZ");
        typeTriple.addProperty("p", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        typeTriple.addProperty("o", "http://ontology/ClassA");
        JsonObject valueTriple = new JsonObject();
        typeTriple.addProperty("s", "http://ontology/ClassA_InstanceZ");
        typeTriple.addProperty("p", "http://ontology/booleanPredicate");
        typeTriple.addProperty("o", "\"true\"^^xsd:boolean");

        array.add(typeTriple);
        array.add(valueTriple);

        JsonObject references = new JsonObject();
        references.add(CLASS_A.getURI(), array);

        ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
        ontologyModel.addFromJson(references, progressIndicator, true);

        OntResource ontResource = CLASS_A.listInstances(true).filterKeep(resource -> resource.getURI() != null && resource.getURI().equals("http://ontology/ClassA_InstanceZ"))
                .toList()
                .get(0);
        Assertions.assertNotNull(ontResource);
        assertTrue(ontResource.hasProperty(ontologyModel.getProperty("http://ontology/booleanPredicate")));
        assertTrue(ontResource.isIndividual());
        assertTrue(ontResource.asIndividual().hasOntClass(CLASS_A, true));
    }

    private boolean areCompatible(String resourceA,
                                  String resourceB) {
        return ontologyModel.areCompatible(Set.of(ontologyModel.getOntResource(resourceA, null)),
                Set.of(ontologyModel.getOntResource(resourceB, null)));
    }

    private Property createProperty(String localName) {
        return ontologyModel.createProperty(createResource("http://ontology#", localName).getURI());
    }

    private OntClass createClass(String name) {
        return ontologyModel.getClass(createResource(name));
    }

    private OntResource createXsdResource(String localName) {
        return createResource("http://www.w3.org/2001/XMLSchema#", localName);
    }

    private OntResource createResource(String localName) {
        return createResource("http://ontology#", localName);
    }

    private OntResource createResource(String namespace, String localName) {
        return ontologyModel.getModel().createOntResource(namespace + localName);
    }
}
