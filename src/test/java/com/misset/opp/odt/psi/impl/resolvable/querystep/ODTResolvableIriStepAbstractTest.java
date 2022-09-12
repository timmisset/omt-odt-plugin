package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableIriStepAbstractTest extends ODTTestCase {

    @Test
    void testResolveIri() {
        final OntResource ontResource = resolveQueryStatementToSingleResult("/<http://ontology#ClassA>");
        Assertions.assertTrue(ontResource.isClass());
    }

    @Test
    void testResolveIriNoLeadingSlash() {
        final Set<OntResource> resources = resolveQueryStatement("<http://ontology#ClassA>");
        Assertions.assertTrue(resources.isEmpty());
    }

}
