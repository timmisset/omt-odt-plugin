package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryReverseStepTest extends OMTOntologyTestCase {
    @Test
    void testResolveWithSubclasses() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassB / ^rdf:type");
        Assertions.assertTrue(resources.contains(createResource("ClassB_INSTANCE")));
        Assertions.assertTrue(resources.contains(createResource("ClassBSub_INSTANCE")));
    }

    @Test
    void testResolveNoSubclassesForOwlThing() {
        final Set<OntResource> resources = resolveQueryStatement("/owl:Thing / ^rdf:type");
        Assertions.assertTrue(resources.contains(createResource("http://www.w3.org/2002/07/owl#", "Thing_INSTANCE")));
    }
}
