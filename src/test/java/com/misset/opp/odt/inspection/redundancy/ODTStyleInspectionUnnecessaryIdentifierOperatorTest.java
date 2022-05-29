package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryIdentifierOperator.REMOVE;
import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryIdentifierOperator.WARNING;

class ODTStyleInspectionUnnecessaryIdentifierOperatorTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionUnnecessaryIdentifierOperator.class);
    }

    @Test
    void testUnnecessaryIdentifierOperatorAsFirstStep() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => . / ont:property;\n");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testUnnecessaryIdentifierOperatorInFilter() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => /ont:ClassA[. / rdf:type == /ont:ClassA];\n");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningInSignatureArgument() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => CALL(.);\n");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningWhenHasFilter() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => . [rdf:type == /ont:ClassA];\n");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningWhenOnlyStep() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => true / CHOOSE WHEN . == true => true OTHERWISE => . END;\n");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testRemoveStep() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => . / ont:property;\n");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains(". / ont:property;"));
        invokeQuickFixIntention(REMOVE);
        Assertions.assertFalse(getFile().getText().contains(". / ont:property;"));
        Assertions.assertTrue(getFile().getText().contains("=> ont:property;"));
    }

    @Test
    void testUnnecessaryIdentifierOperatorDoesntRemoveFilter() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => /ont:ClassA / . [rdf:type == /ont:ClassA];\n");
        configureByText(content);
        assertHasWarning(WARNING);
        invokeQuickFixIntention(REMOVE);
        Assertions.assertTrue(getFile().getText().contains("/ont:ClassA / [rdf:type == /ont:ClassA];"));
    }
}
