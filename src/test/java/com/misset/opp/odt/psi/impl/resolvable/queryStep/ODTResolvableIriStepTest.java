package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableIriStepTest extends OntologyTestCase {

    @Test
    void testResolveIri() {
        final OntResource ontResource = resolveQueryStatementToSingleResult("/<http://ontologie#ClassA>");
        Assertions.assertTrue(ontResource.isClass());
    }

    @Test
    void testResolveIriNoLeadingSlash() {
        final Set<OntResource> resources = resolveQueryStatement("<http://ontologie#ClassA>");
        Assertions.assertTrue(resources.isEmpty());
    }

}
