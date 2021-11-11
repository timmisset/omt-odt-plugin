package com.misset.opp.ttl;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class OppModelTest extends OntologyTestCase {
    private OntClass CLASS_A;
    private Individual CLASS_A_INDIVIDUAL;

    @BeforeEach
    protected void setUp() {
        setOntologyModel();
        CLASS_A = createClass("ClassA");
        CLASS_A_INDIVIDUAL = CLASS_A.createIndividual();
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
    void listPredicateObjectsOnClass() {
        final Set<Statement> statements = oppModel.listPredicateObjects(CLASS_A);
        assertStatementContains(statements, createProperty("booleanPredicate"), createXsdResource("boolean"));
        // when traversing (forward) a class itself, the returned predicates are all available on the class and superclasses
        // moreover, the class contains a rdf:type for all inherited classes
        assertStatementContains(statements, oppModel.RDF_TYPE, oppModel.OWL_CLASS);
    }

    @Test
    void listPredicateObjectsOnIndividual() {
        final Set<Statement> statements = oppModel.listPredicateObjects(CLASS_A_INDIVIDUAL);
        assertStatementContains(statements, createProperty("booleanPredicate"), createXsdResource("boolean"));
        assertStatementNotContains(statements, oppModel.RDF_TYPE, oppModel.OWL_CLASS);
        assertStatementContains(statements, oppModel.RDF_TYPE, CLASS_A);
        assertStatementContains(statements, oppModel.RDF_TYPE, oppModel.OWL_THING);
    }

    @Test
    void listPredicateOnClass() {
        final Set<Property> predicates = oppModel.listPredicates(CLASS_A);
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(oppModel.RDF_TYPE));
        Assertions.assertTrue(predicates.contains(oppModel.RDFS_SUBCLASS_OF));
    }

    @Test
    void listPredicateOnIndividual() {
        final Set<Property> predicates = oppModel.listPredicates(CLASS_A_INDIVIDUAL);
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(oppModel.RDF_TYPE));
        Assertions.assertFalse(predicates.contains(oppModel.RDFS_SUBCLASS_OF));
    }

    @Test
    void listPredicatesMultipleClasses() {
        final Set<Resource> predicates = oppModel.listPredicates(
                List.of(CLASS_A, createClass("ClassB")));
        Assertions.assertTrue(predicates.contains(createProperty("booleanPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
        Assertions.assertTrue(predicates.contains(oppModel.RDF_TYPE));
    }

    @Test
    void listPredicatesSubclasses() {
        final Set<Property> predicates = oppModel.listPredicates(createClass("ClassBSub"));
        Assertions.assertTrue(predicates.contains(createProperty("numberPredicate")));
        Assertions.assertTrue(predicates.contains(createProperty("stringPredicate")));
    }

    @Test
    void testListSubjectsRdfType() {
        final Set<OntResource> resources = oppModel.listSubjects(oppModel.RDF_TYPE, Set.of(CLASS_A));
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
        final Set<OntResource> resources = oppModel.listSubjects(oppModel.RDFS_SUBCLASS_OF,
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
                resource -> resource.equals(oppModel.XSD_BOOLEAN_INSTANCE)
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
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A_INDIVIDUAL), oppModel.RDF_TYPE);
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(CLASS_A))
        );
    }

    @Test
    void testListObjectsRdfTypeOnClass() {
        final Set<OntResource> resources = oppModel.listObjects(Set.of(CLASS_A), oppModel.RDF_TYPE);
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(oppModel.OWL_CLASS))
        );
    }

    @Test
    void testListAcceptableTypes() {
        Assertions.assertTrue(oppModel.listAcceptableTypes(CLASS_A)
                .stream()
                .anyMatch(resource -> resource.equals(oppModel.getIndividual("http://ontology#ClassA_InstanceA"))));
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
}
