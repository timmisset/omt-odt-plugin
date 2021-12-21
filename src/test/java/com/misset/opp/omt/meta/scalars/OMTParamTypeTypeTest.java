package com.misset.opp.omt.meta.scalars;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.scalars.OMTParamTypeType.UNKNOWN_PREFIX;

class OMTParamTypeTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(OMTValueInspection.class);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testUnknownPrefixError() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       params:\n" +
                "       - $param (ont:ClassA)\n" +
                "";
        configureByText(content);
        assertHasError(UNKNOWN_PREFIX);
    }

    @Test
    void testUnknownPrefixForDestructed() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       params:\n" +
                "       - name: $param\n" +
                "         type: ont:ClassA\n" +
                "";
        configureByText(content);
        assertHasError(UNKNOWN_PREFIX);
    }

    @Test
    void testUnknownIriForDestructed() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: ont:ClassUnknown\n" +
                "");
        configureByText(content);
        assertHasError("Could not find resource <http://ontology#ClassUnknown> in the Opp Model");
    }

    @Test
    void testUnknownQualifiedIriForDestructed() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: <http://ontology#ClassUnknown>\n" +
                "");
        configureByText(content);
        assertHasError("Could not find resource <http://ontology#ClassUnknown> in the Opp Model");
    }

    @Test
    void testExpectedClassOrType() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: <http://ontology#booleanPredicate>\n" +
                "");
        configureByText(content);
        assertHasError("Expected a class or type");
    }

    @Test
    void testHasTypeCompletion() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: <caret>\n" +
                "");
        configureByText(content);
        LookupElement[] lookupElements = myFixture.completeBasic();
        List<String> collect = Arrays.stream(lookupElements).map(LookupElement::getLookupString).collect(Collectors.toList());

        assertContainsElements(collect, "ont:ClassA", "xsd:string", "string");
        assertDoesntContain(collect, "ont:booleanPredicate");
    }
}