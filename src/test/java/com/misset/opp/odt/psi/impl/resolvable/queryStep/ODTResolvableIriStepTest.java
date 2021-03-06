package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableIriStepTest extends OMTOntologyTestCase {

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
