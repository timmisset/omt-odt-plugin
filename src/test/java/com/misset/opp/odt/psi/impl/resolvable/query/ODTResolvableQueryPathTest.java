package com.misset.opp.odt.psi.impl.resolvable.query;

import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableQueryPathTest extends OMTOntologyTestCase {

    @Test
    void testRootPathResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.equals(createClass("ClassA")) && resource instanceof OntClass)
        );
    }

    @Test
    void testRootPathResolvesTypeReverse() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createClass("ClassA"))
        ));
    }

    @Test
    void testRootPathResolvesTypeForward() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / rdf:type");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> createClass("ClassA").equals(resource))
        );
    }

    @Test
    void testCurieStepResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / ont:booleanPredicate");
        Assertions.assertTrue(resources.contains(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testCurieStepResolvesReversedToInstance() {
        final Set<OntResource> resources = resolveQueryStatement("true / ^ont:booleanPredicate");
        Assertions.assertFalse(resources.isEmpty());
        Assertions.assertTrue(
                resources.stream().allMatch(OntResource::isIndividual)
        );
    }

    @Test
    void testCurieStepResolvesReversedToClass() {
        // when the query points to a property itself, not an instance value, model data is returned
        final Set<OntResource> resources = resolveQueryStatement("/xsd:boolean / ^ont:booleanPredicate");
        Assertions.assertFalse(resources.isEmpty());
        Assertions.assertTrue(
                resources.stream().allMatch(OntResource::isClass)
        );
    }

    @Test
    void testNegatedStep() {
        final Set<OntResource> resources = resolveQueryStatement("NOT IN(/ont:ClassA)");
        Assertions.assertTrue(resources.contains(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testNegatedStepTerminal() {
        final Set<OntResource> resources = resolveQueryStatement("IN(/ont:ClassA) / NOT");
        Assertions.assertTrue(resources.contains(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testSubqueryWithoutPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate)*");
        Assertions.assertTrue(resources.contains(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testSubqueryWithPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate / ^ont:booleanPredicate)*");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createClass("ClassA"))
        ));
    }

    @Test
    void testSubqueryWithPathAndFilter() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate / ^ont:booleanPredicate[rdf:type == /ont:ClassA])*");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createClass("ClassA"))
        ));
    }

}
