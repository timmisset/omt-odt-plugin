package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.testcase.ODTTestCase;
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
        Assertions.assertTrue(resources.contains(OntologyModelConstants.getXsdBooleanInstance().getURI()));
    }

}
