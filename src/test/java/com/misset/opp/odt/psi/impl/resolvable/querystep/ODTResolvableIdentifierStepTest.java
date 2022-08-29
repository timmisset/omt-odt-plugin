package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableIdentifierStepTest extends OMTOntologyTestCase {

    @Test
    void testIdentifierStepReturnsIndividual() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / .");
        Assertions.assertTrue(resource.isIndividual());
        Assertions.assertTrue(resource.asIndividual().hasOntClass(createClass("ClassA")));
    }

    @Test
    void testIdentifierStepReturnsClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / .");
        Assertions.assertTrue(resource.isClass());
        Assertions.assertEquals(createClass("ClassA"), resource);
    }



}
