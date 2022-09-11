package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

class ODTResolvableQueryPathTest extends ODTTestCase {

    @Test
    void testRootPathResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> resource.getURI().equals(createOntologyUri("ClassA")) && resource instanceof OntClass)
        );
    }

    @Test
    void testRootPathResolvesTypeReverse() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createOntologyUri("ClassA"))
        ));
    }

    @Test
    void testRootPathResolvesTypeForward() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / rdf:type");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> createOntologyUri("ClassA").equals(resource.getURI())));
    }

    @Test
    void testCurieStepResolves() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / ont:booleanPredicate");
        Assertions.assertTrue(resources.contains(OppModelConstants.getXsdBooleanInstance()));
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
        Assertions.assertTrue(resources.contains(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testNegatedStepTerminal() {
        final Set<OntResource> resources = resolveQueryStatement("IN(/ont:ClassA) / NOT");
        Assertions.assertTrue(resources.contains(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testSubqueryWithoutPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate)*");
        Assertions.assertTrue(resources.contains(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testSubqueryWithPath() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate / ^ont:booleanPredicate)*");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createOntologyUri("ClassA"))
        ));
    }

    @Test
    void testSubqueryWithPathAndFilter() {
        final Set<OntResource> resources = resolveQueryStatement("/ont:ClassA / ^rdf:type / (ont:booleanPredicate / ^ont:booleanPredicate[rdf:type == /ont:ClassA])*");
        Assertions.assertTrue(resources.stream().anyMatch(
                resource -> isIndividualOfClass(resource, createOntologyUri("ClassA"))
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ont:predicate",
            "ont:predicate / ont:anotherPredicate",
            "DEFINE QUERY query => ont:predicate",
            "LOG",
            "^ont:predicate",
            "NOT",
            "NOT(ont:predicate)"
    })
    void testRequiresInput(String content) {
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            ODTResolvableQueryPath resolvableQueryPath = PsiTreeUtil.findChildOfType(odtFileTest, ODTResolvableQueryPath.class);
            assertTrue(resolvableQueryPath.requiresInput());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/ont:predicate",
            "/ont:predicate / ont:anotherPredicate",
            "DEFINE QUERY query => /ont:predicate",
            "$value",
            "NOT(true)"
    })
    void testDoesntRequireInput(String content) {
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            ODTResolvableQueryPath resolvableQueryPath = PsiTreeUtil.findChildOfType(odtFileTest, ODTResolvableQueryPath.class);
            assertFalse(resolvableQueryPath.requiresInput());
        });
    }

    private boolean isIndividualOfClass(Resource resource, String classResource) {
        if (resource instanceof Individual) {
            return ((Individual) resource).getOntClass().getURI().equals(classResource);
        }
        return false;
    }
}
