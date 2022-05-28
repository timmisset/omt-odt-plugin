package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryForwardStepTest extends OMTOntologyTestCase {

    @Test
    void testResolveWithoutSubclasses() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassB");
        Assertions.assertTrue(resources.contains(createResource("ClassB")));
        Assertions.assertFalse(resources.contains(createResource("ClassBSub")));
    }

    @Test
    void testResolveWithSubclasses() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ^rdf:type / ont:classB");
        Assertions.assertTrue(resources.contains(createResource("ClassB_INSTANCE")));
        Assertions.assertTrue(resources.contains(createResource("ClassBSub_INSTANCE")));
    }

    @Test
    void testResolveWithSubclassesEmptyResponseWhenInvalidPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ^rdf:type / ont:claaaaaassB");
        assertEmpty(resources);
    }
}
