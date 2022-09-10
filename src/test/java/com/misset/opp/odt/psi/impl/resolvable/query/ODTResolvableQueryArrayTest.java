package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryArrayTest extends ODTTestCase {

    @Test
    void testRootPathResolves() {
        final Set<String> resources = getUris(resolveQueryStatement("/ont:ClassA | /ont:ClassB"));
        Assertions.assertTrue(resources.contains(createOntologyUri("ClassA")));
        Assertions.assertTrue(resources.contains(createOntologyUri("ClassB")));
    }

    @Test
    void testPropertiesResolve() {
        final Set<String> resources = getUris(
                resolveQueryStatement("/ont:ClassA / ^rdf:type / ont:booleanPredicate | ont:classPredicate"));
        Assertions.assertTrue(resources.contains(createOntologyUri("ClassBSub_INSTANCE")));
        Assertions.assertTrue(resources.contains(OppModelConstants.getXsdBooleanInstance().getURI()));
    }

}
