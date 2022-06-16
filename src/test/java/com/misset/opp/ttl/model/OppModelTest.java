package com.misset.opp.ttl.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.progress.ProgressIndicator;
import com.misset.opp.testCase.OMTOntologyTestCase;
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
    private OntClass CLASS_A;
    private Individual CLASS_A_INDIVIDUAL;

    @BeforeEach
    protected void setUp() {
        setOntologyModel();
        CLASS_A = createClass("ClassA");
        CLASS_A_INDIVIDUAL = oppModel.createIndividual(CLASS_A, CLASS_A.getURI() + "_INSTANCE");
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
        Assertions.assertTrue(predicates.contains(OppModelConstants.RDF_TYPE));
        Assertions.assertTrue(predicates.contains(OppModelConstants.RDFS_SUBCLASS_OF));
    }

    @Test
    void listPredicateOnIndividual() {
        final Set<Property> predicates = oppModel.listPredicates(CLASS_A_INDIVIDUAL);
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(OppModelConstants.RDF_TYPE));
        Assertions.assertFalse(predicates.contains(OppModelConstants.RDFS_SUBCLASS_OF));
    }

    @Test
    void listPredicatesMultipleClasses() {
        final Set<Property> predicates = oppModel.listPredicates(
                Set.of(CLASS_A, createClass("ClassB")));
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
        Assertions.assertTrue(predicates.contains(OppModelConstants.RDF_TYPE));
    }

    @Test
    void listPredicatesSubclasses() {
        final Set<Property> predicates = oppModel.listPredicates(createClass("ClassBSub"));
        Assertions.assertTrue(predicates.contains(createProperty("numberPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
    }

    @Test
    void testListSubjectsRdfType() {
        final Set<OntResource> resources = oppModel.listSubjects(OppModelConstants.RDF_TYPE, Set.of(CLASS_A));
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
        final Set<OntResource> resources = oppModel.listSubjects(OppModelConstants.RDFS_SUBCLASS_OF,
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
                resource -> resource.equals(OppModelConstants.XSD_BOOLEAN_INSTANCE)
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
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A_INDIVIDUAL), OppModelConstants.RDF_TYPE);
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(CLASS_A))
        );
    }

    @Test
    void testListObjectsRdfTypeOnClass() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A), OppModelConstants.RDF_TYPE);
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(OppModelConstants.OWL_CLASS))
        );
    }

    @Test
    void testInstanceValues() {
        final Individual individual = oppModel.getIndividual("http://ontology#ClassA_InstanceA");
        final RDFNode booleanPredicate = individual.listPropertyValues(
                oppModel.getProperty(createProperty("booleanPredicate"))
        ).next();
        Assertions.assertTrue(booleanPredicate.isLiteral());
        Assertions.assertTrue(booleanPredicate.asLiteral().getBoolean());
    }

    @Test
    void testAreCompatible() {
        Assertions.assertTrue(areCompatible("http://ontology#ClassA_InstanceA", "http://ontology#ClassA_InstanceA"));
        Assertions.assertTrue(areCompatible("http://ontology#ClassA", "http://ontology#ClassA"));
        Assertions.assertFalse(areCompatible("http://ontology#ClassA_InstanceA", "http://ontology#ClassA"));

        Assertions.assertTrue(areCompatible("http://ontology#ClassB", "http://ontology#ClassBSub"));
        Assertions.assertFalse(areCompatible("http://ontology#ClassBSub", "http://ontology#ClassB"));

        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#number",
                "http://www.w3.org/2001/XMLSchema#decimal"));
        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#number",
                "http://www.w3.org/2001/XMLSchema#integer"));
        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#number",
                "http://www.w3.org/2001/XMLSchema#number"));
        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#integer",
                "http://www.w3.org/2001/XMLSchema#integer"));
        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#decimal",
                "http://www.w3.org/2001/XMLSchema#decimal"));
        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#decimal",
                "http://www.w3.org/2001/XMLSchema#integer"));
        Assertions.assertFalse(areCompatible("http://www.w3.org/2001/XMLSchema#integer",
                "http://www.w3.org/2001/XMLSchema#decimal"));

        Assertions.assertTrue(areCompatible("http://www.w3.org/2001/XMLSchema#dateTime",
                "http://www.w3.org/2001/XMLSchema#date"));
        Assertions.assertFalse(areCompatible("http://www.w3.org/2001/XMLSchema#date",
                "http://www.w3.org/2001/XMLSchema#dateTime"));
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
