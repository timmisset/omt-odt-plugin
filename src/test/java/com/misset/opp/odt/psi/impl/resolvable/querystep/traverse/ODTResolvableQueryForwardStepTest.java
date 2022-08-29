package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
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
        Assertions.assertTrue(resources.stream().allMatch(OppModel.INSTANCE::isIndividual));
    }

    @Test
    void testResolveWithSubclassesEmptyResponseWhenInvalidPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ^rdf:type / ont:claaaaaassB");
        assertEmpty(resources);
    }

    @Test
    void testResolveWithoutTypeShouldResolveToClass() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassD / ont:classB");
        Assertions.assertTrue(resources.contains(createResource("ClassB")));
        Assertions.assertTrue(resources.stream().allMatch(OppModel.INSTANCE::isClass));
    }
}
