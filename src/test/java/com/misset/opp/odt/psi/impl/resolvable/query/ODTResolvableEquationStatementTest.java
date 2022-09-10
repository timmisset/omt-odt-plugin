package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableEquationStatementTest extends ODTTestCase {

    @Test
    void testFilterRdfTypeLeading() {
        final OntResource resource = resolveQueryStatementToSingleResult(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[rdf:type == /ont:ClassA]");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createOntologyUri("ClassA")));
    }

    @Test
    void testFilterRdfTypeTrailing() {
        final OntResource resource = resolveQueryStatementToSingleResult(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[/ont:ClassA == rdf:type]");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createOntologyUri("ClassA")));
    }

    @Test
    void testFilterRdfTypeNegated() {
        final Set<OntResource> resources = resolveQueryStatement(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[NOT /ont:ClassA == rdf:type]");
        Assertions.assertEquals(2, resources.size());
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createOntologyUri("ClassB"))));
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createOntologyUri("ClassBSub"))));
    }

    @Test
    void testFilterSubClass() {
        final Set<OntResource> resources = resolveQueryStatement(
                "/ont:ClassB / ^rdf:type[/ont:ClassBSub == rdf:type]");
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createOntologyUri("ClassBSub"))));
    }

    @Test
    void testFilterOwlThing() {
        final Set<OntResource> resources = resolveQueryStatement(
                "/owl:Thing / ^rdf:type[/ont:ClassBSub == rdf:type]");
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createOntologyUri("ClassBSub"))));
    }

    @Test
    void testFilterByValue() {
        final OntResource resource = resolveQueryStatementToSingleResult(
                "/ont:ClassA / ^rdf:type[ont:booleanPredicate == true]");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createOntologyUri("ClassA")));
    }

    @Test
    void testFilterByValueIncompatibleTypesReturnsEmptySet() {
        final Set<OntResource> resources = resolveQueryStatement(
                "/ont:ClassA / ^rdf:type[ont:booleanPredicate == 'true']");
        Assertions.assertEquals(0, resources.size());
    }
}
