package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ODTResolvableEquationStatementTest extends OntologyTestCase {

    @Test
    void testFilterRdfTypeLeading() {
        final Set<OntResource> resources = resolveQueryStatement(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[rdf:type == /ont:ClassA]");
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.stream().findFirst().get().asIndividual().hasOntClass(createClass("ClassA")));
    }

    @Test
    void testFilterRdfTypeTrailing() {
        final Set<OntResource> resources = resolveQueryStatement(
                "(/ont:ClassA | /ont:ClassB) / ^rdf:type[/ont:ClassA == rdf:type]");
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.stream().findFirst().get().asIndividual().hasOntClass(createClass("ClassA")));
    }

}
