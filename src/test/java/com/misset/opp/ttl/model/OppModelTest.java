package com.misset.opp.ttl.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.progress.ProgressIndicator;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.constants.XSD;
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

class OppModelTest extends OMTOntologyTestCase {
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

    @BeforeEach
    protected void setUp() {
        setOntologyModel();
        CLASS_A = createClass("ClassA");
        CLASS_B = createClass("ClassB");
        CLASS_BSUB = createClass("ClassBSub");
        CLASS_BSUB.setSuperClass(CLASS_B);
        CLASS_A_INDIVIDUAL = oppModel.createIndividual(CLASS_A, CLASS_A.getURI() + "_INSTANCE");
        CLASS_B_INDIVIDUAL = oppModel.createIndividual(CLASS_B, CLASS_B.getURI() + "_INSTANCE");
        CLASS_BSUB_INDIVIDUAL = oppModel.createIndividual(CLASS_BSUB, CLASS_BSUB.getURI() + "_INSTANCE");
    }

    @Override
    @AfterEach
    protected void tearDown() {
        /*
            noop
            do not interact with the fixture for this Test class
        */
    }

    @Test
    void listPredicateOnClass() {
        final Set<Property> predicates = oppModel.listPredicates(CLASS_A);
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(OppModelConstants.getRdfType()));
        Assertions.assertTrue(predicates.contains(OppModelConstants.getRdfsSubclassOf()));
    }

    @Test
    void listPredicateOnIndividual() {
        final Set<Property> predicates = oppModel.listPredicates(CLASS_A_INDIVIDUAL);
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(OppModelConstants.getRdfType()));
        Assertions.assertFalse(predicates.contains(OppModelConstants.getRdfsSubclassOf()));
    }

    @Test
    void listPredicatesMultipleClasses() {
        final Set<Property> predicates = oppModel.listPredicates(
                Set.of(CLASS_A, createClass("ClassB")));
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
        Assertions.assertTrue(predicates.contains(OppModelConstants.getRdfType()));
    }

    @Test
    void listPredicatesSubclasses() {
        final Set<Property> predicates = oppModel.listPredicates(createClass("ClassBSub"));
        Assertions.assertTrue(predicates.contains(createProperty("numberPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
    }

    @Test
    void testListSubjectsRdfType() {
        final Set<OntResource> resources = oppModel.listSubjects(OppModelConstants.getRdfType(), Set.of(CLASS_A));
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof Individual && ((Individual) resource).hasOntClass(CLASS_A)
        ));
    }

    @Test
    void testListSubjectsResourcePredicate() {
        final Set<OntResource> resources = oppModel.listSubjects(createProperty("booleanPredicate"),
                Set.of(createXsdResource("boolean")));
        Assertions.assertTrue(resources.stream().anyMatch(CLASS_A::equals));
    }

    @Test
    void testListSubjectsClassPredicate() {
        final Set<OntResource> resources = oppModel.listSubjects(OppModelConstants.getRdfsSubclassOf(),
                Set.of(createResource("ClassB")));
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof OntClass && resource.equals(createResource(
                        "ClassBSub"))
        ));
    }

    @Test
    void testListObjectsPredicate() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A_INDIVIDUAL),
                createProperty("booleanPredicate"));
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(OppModelConstants.getXsdBooleanInstance())
        ));
    }

    @Test
    void testListObjectsInstanceToInstance() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A_INDIVIDUAL),
                createProperty("classPredicate"));
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource instanceof Individual && ((Individual) resource).hasOntClass(createClass(
                        "ClassBSub"))
        ));
    }

    @Test
    void testListObjectsRdfTypeOnIndividual() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A_INDIVIDUAL), OppModelConstants.getRdfType());
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(CLASS_A))
        );
    }

    @Test
    void testListObjectsRdfTypeOnClass() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A), OppModelConstants.getRdfType());
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(OppModelConstants.getOwlClass()))
        );
    }

    @Test
    void testInstanceValues() {
        final Individual individual = oppModel.getIndividual(CLASS_A_INSTANCE_A);
        final RDFNode booleanPredicate = individual.listPropertyValues(
                oppModel.getProperty(createProperty("booleanPredicate"))
        ).next();
        Assertions.assertTrue(booleanPredicate.isLiteral());
        Assertions.assertTrue(booleanPredicate.asLiteral().getBoolean());
    }

    @Test
    void testAnOppClassIsAnOwlClass() {
        final OntClass ontClass = oppModel.getClass(CLASS_A.getURI());
        assertEquals(ontClass.getRDFType(), OppModelConstants.getOwlClass());
    }

    @Test
    void testAnOppClassIsNotASubclassOfAnOwlClass() {
        final Individual individual = oppModel.getIndividual(CLASS_A_INSTANCE_A);
        OntClass ontClass = oppModel.toClass(individual);
        assertFalse(ontClass.listSuperClasses(false).toList().contains(OppModelConstants.getOwlClass()));
    }

    @Test
    void testAnOppClassIsASubclassOfAnOwlThing() {
        final Individual individual = oppModel.getIndividual(CLASS_A_INSTANCE_A);
        OntClass ontClass = oppModel.toClass(individual);
        assertTrue(ontClass.listSuperClasses(false).toList().contains(OppModelConstants.getOwlThingClass()));
    }

    @Test
    void testAreCompatibleTrueWhenEqualIndividuals() {
        Assertions.assertTrue(areCompatible(CLASS_A_INSTANCE_A, CLASS_A_INSTANCE_A));
    }

    @Test
    void testAreCompatibleTrueInstanceWithOWLThing() {
        Assertions.assertTrue(areCompatible(OppModelConstants.getOwlThingInstance().getURI(), CLASS_A_INSTANCE_A));
        Assertions.assertTrue(areCompatible(CLASS_A_INSTANCE_A, OppModelConstants.getOwlThingInstance().getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceWithOWLClass() {
        Assertions.assertFalse(areCompatible(OppModelConstants.getOwlClass().getURI(), CLASS_A_INSTANCE_A));
        Assertions.assertFalse(areCompatible(CLASS_A_INSTANCE_A, OppModelConstants.getOwlClass().getURI()));
    }

    @Test
    void testAreCompatibleTrueClassWithOWLClass() {
        Assertions.assertTrue(areCompatible(OppModelConstants.getOwlClass().getURI(), CLASS_A.getURI()));
        Assertions.assertTrue(areCompatible(OppModelConstants.getRdfsClass().getURI(), CLASS_A.getURI()));
        Assertions.assertTrue(areCompatible(OppModelConstants.getRdfsResource().getURI(), CLASS_A.getURI()));
    }

    @Test
    void testAreCompatibleFalseOWLClassWithClass() {
        Assertions.assertFalse(areCompatible(CLASS_A.getURI(), OppModelConstants.getOwlClass().getURI()));
    }

    @Test
    void testAreCompatibleFalseClassWithOWLThing() {
        Assertions.assertFalse(areCompatible(OppModelConstants.getOwlThingInstance().getURI(), ONTOLOGY_CLASS_A));
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_A, OppModelConstants.getOwlThingInstance().getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceWithClass() {
        Assertions.assertFalse(areCompatible(CLASS_A_INSTANCE_A, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseAny2DifferentClasses() {
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_A, ONTOLOGY_CLASS_B));
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_B, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseIdenticalClasses() {
        // Classes that are identical are not compatible
        // Classes are hardly ever directly compatible unless the requirement is owl:Class and the provided
        // is an instance of owl:Class which is any class with a rdf:type owl:Class
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_A, ONTOLOGY_CLASS_A));
    }

    @Test
    void testAreCompatibleFalseClassWithSubclass() {
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_B, ONTOLOGY_CLASS_BSUB));
        Assertions.assertFalse(areCompatible(ONTOLOGY_CLASS_BSUB, ONTOLOGY_CLASS_B));
    }

    @Test
    void testAreCompatibleTrueInstanceOfSubclass() {
        Assertions.assertTrue(areCompatible(CLASS_B_INDIVIDUAL.getURI(), CLASS_BSUB_INDIVIDUAL.getURI()));
    }

    @Test
    void testAreCompatibleFalseInstanceOfSuperclass() {
        Assertions.assertFalse(areCompatible(CLASS_BSUB_INDIVIDUAL.getURI(), CLASS_B_INDIVIDUAL.getURI()));
    }

    @Test
    void testAreCompatibleNumbers() {
        // hierarchy is: integer subclassOf decimal subclassOf number
        Assertions.assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.DECIMAL_INSTANCE.getUri()));
        Assertions.assertTrue(areCompatible(XSD.DECIMAL_INSTANCE.getUri(),
                XSD.INTEGER_INSTANCE.getUri()));
        Assertions.assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.INTEGER_INSTANCE.getUri()));
        Assertions.assertTrue(areCompatible(XSD.NUMBER_INSTANCE.getUri(),
                XSD.NUMBER_INSTANCE.getUri()));

        Assertions.assertFalse(areCompatible(XSD.DECIMAL_INSTANCE.getUri(),
                XSD.NUMBER_INSTANCE.getUri()));
        Assertions.assertFalse(areCompatible(XSD.INTEGER_INSTANCE.getUri(),
                XSD.DECIMAL_INSTANCE.getUri()));
    }

    @Test
    void testAreCompatibleDates() {
        // hierarchy is: date subclassOf dateTime
        Assertions.assertTrue(areCompatible(XSD.DATETIME_INSTANCE.getUri(),
                XSD.DATE_INSTANCE.getUri()));

        Assertions.assertFalse(areCompatible(XSD.DATE_INSTANCE.getUri(),
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
        oppModel.addFromJson(references, progressIndicator, true);

        OntResource ontResource = CLASS_A.listInstances(true).filterKeep(resource -> resource.getURI() != null && resource.getURI().equals("http://ontology/ClassA_InstanceZ"))
                .toList()
                .get(0);
        Assertions.assertNotNull(ontResource);
        Assertions.assertTrue(ontResource.hasProperty(oppModel.getProperty("http://ontology/booleanPredicate")));
        Assertions.assertTrue(ontResource.isIndividual());
        Assertions.assertTrue(ontResource.asIndividual().hasOntClass(CLASS_A, true));
    }

    private boolean areCompatible(String resourceA,
                                  String resourceB) {
        return oppModel.areCompatible(Set.of(oppModel.getOntResource(resourceA, null)),
                Set.of(oppModel.getOntResource(resourceB, null)));
    }
}
