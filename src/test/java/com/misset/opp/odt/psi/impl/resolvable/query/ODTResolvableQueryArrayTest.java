package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryArrayTest extends OMTOntologyTestCase {

    @Test
    void testRootPathResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA | /ont:ClassB");
        Assertions.assertTrue(resources.contains(createResource("ClassA")));
        Assertions.assertTrue(resources.contains(createResource("ClassB")));
    }

}
