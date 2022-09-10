package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableIdentifierStepTest extends ODTTestCase {

    @Test
    void testIdentifierStepReturnsIndividual() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / .");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createOntologyUri("ClassA")));
    }

    @Test
    void testIdentifierStepReturnsClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / .");
        Assertions.assertTrue(resource.isClass());
        Assertions.assertEquals(createOntologyUri("ClassA"), resource.getURI());
    }



}
