package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ODTResolvableIdentifierStepTest extends OntologyTestCase {

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
