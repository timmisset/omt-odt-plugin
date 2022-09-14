package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryReverseStepAbstractTest extends ODTTestCase {
    @Test
    void testResolveWithSubclasses() {
        final Set<String> resources = getUris(resolveQueryStatement("/ont:ClassB / ^rdf:type"));
        Assertions.assertTrue(resources.contains(createOntologyUri("ClassB_INSTANCE")));
        Assertions.assertTrue(resources.contains(createOntologyUri("ClassBSub_INSTANCE")));
    }

    @Test
    void testResolveNoSubclassesForOwlThing() {
        final Set<OntResource> resources = resolveQueryStatement("/owl:Thing / ^rdf:type");
        Assertions.assertTrue(getUris(resources).contains("http://www.w3.org/2002/07/owl#Thing_INSTANCE"));
    }
}
