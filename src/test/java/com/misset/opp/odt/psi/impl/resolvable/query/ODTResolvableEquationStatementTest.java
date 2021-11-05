package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableEquationStatementTest extends OntologyTestCase {

    @Test
    void testFilterRdfTypeLeading() {
        final OntResource resource = resolveQueryStatementToSingleResult(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[rdf:type == /ont:ClassA]");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createClass("ClassA")));
    }

    @Test
    void testFilterRdfTypeTrailing() {
        final OntResource resource = resolveQueryStatementToSingleResult(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[/ont:ClassA == rdf:type]");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createClass("ClassA")));
    }

    @Test
    void testFilterRdfTypeNegated() {
        final Set<OntResource> resources = resolveQueryStatement(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[NOT /ont:ClassA == rdf:type]");
        Assertions.assertEquals(2, resources.size());
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createClass("ClassB"))));
        Assertions.assertTrue(resources.stream().anyMatch(resource -> resource.asIndividual().hasOntClass(createClass("ClassBSub"))));
    }

}
