package com.misset.opp.omt.meta.scalars;

import com.intellij.codeInsight.lookup.LookupElement;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.scalars.OMTParamTypeMetaType.UNKNOWN_PREFIX;

class OMTParamTypeMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
    }

    @Test
    void testUnknownPrefixError() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       params:\n" +
                "       - $param (ont:ClassA)\n" +
                "";
        configureByText(content);
        inspection.assertHasError(UNKNOWN_PREFIX);
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
        inspection.assertHasError(UNKNOWN_PREFIX);
    }

    @Test
    void testUnknownIriForDestructed() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: ont:ClassUnknown\n" +
                "");
        configureByText(content);
        inspection.assertHasError("Could not find resource <http://ontology#ClassUnknown> in the Opp Model");
    }

    @Test
    void testUnknownQualifiedIriForDestructed() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: <http://ontology#ClassUnknown>\n" +
                "");
        configureByText(content);
        inspection.assertHasError("Could not find resource <http://ontology#ClassUnknown> in the Opp Model");
    }

    @Test
    void testExpectedClassOrType() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- name: $param\n" +
                "  type: <http://ontology#booleanPredicate>\n" +
                "");
        configureByText(content);
        inspection.assertHasError("Expected a class or type");
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
