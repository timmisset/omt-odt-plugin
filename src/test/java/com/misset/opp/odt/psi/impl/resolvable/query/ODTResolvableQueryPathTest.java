package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryPathTest extends OntologyTestCase {

    @Test
    void testRootPathResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(createResource("ClassA")) && resource instanceof OntClass)
        );
    }

    @Test
    void testRootPathResolvesReverse() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createResource("ClassA"))
        ));
    }

    @Test
    void testCurieStepResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / ont:booleanPredicate");
        Assertions.assertTrue(resources.contains(createXsdResource("boolean")));
    }

    @Test
    void testCurieStepResolvesReversed() {
        final Set<OntResource> resources = resolveQueryStatement("/xsd:boolean / ^ont:booleanPredicate");
        Assertions.assertTrue(resources.contains(createXsdResource("boolean")));
    }

    @Test
    void testNegatedStep() {
        final Set<OntResource> resources = resolveQueryStatement("NOT IN(/ont:ClassA)");
        Assertions.assertTrue(resources.contains(createXsdResource("boolean")));
    }

    @Test
    void testNegatedStepTerminal() {
        final Set<OntResource> resources = resolveQueryStatement("IN(/ont:ClassA) / NOT");
        Assertions.assertTrue(resources.contains(createXsdResource("boolean")));
    }

}
