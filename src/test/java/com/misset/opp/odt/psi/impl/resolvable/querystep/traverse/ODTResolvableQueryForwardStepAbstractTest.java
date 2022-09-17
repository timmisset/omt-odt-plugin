package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryForwardStepAbstractTest extends ODTTestCase {

    @Test
    void testResolveWithoutSubclasses() {
        final Set<String> resourceUris = getUris(resolveQueryStatement("/ont:ClassB"));
        Assertions.assertTrue(resourceUris.contains(createOntologyUri("ClassB")));
        Assertions.assertFalse(resourceUris.contains(createOntologyUri("ClassBSub")));
    }

    @Test
    void testResolveWithSubclasses() {
        Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ^rdf:type / ont:classB");
        final Set<String> resourceUris = getUris(resources);
        Assertions.assertTrue(resourceUris.contains(createOntologyUri("ClassB_INSTANCE")));
        Assertions.assertTrue(resourceUris.contains(createOntologyUri("ClassBSub_INSTANCE")));
        Assertions.assertTrue(resources.stream().allMatch(OntologyModel.getInstance()::isIndividual));
    }

    @Test
    void testResolveWithSubclassesEmptyResponseWhenInvalidPath() {
        assertEmpty(resolveQueryStatement("/ont:ClassD / ^rdf:type / ont:claaaaaassB"));
    }

    @Test
    void testResolveWithoutTypeShouldResolveToClass() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ont:classB");
        Assertions.assertTrue(getUris(resources).contains(createOntologyUri("ClassB")));
        Assertions.assertTrue(resources.stream().allMatch(OntologyModel.getInstance()::isClass));
    }
}
