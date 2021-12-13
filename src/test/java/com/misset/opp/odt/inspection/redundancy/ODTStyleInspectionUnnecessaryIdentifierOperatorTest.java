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
    void testUnnecessaryBaseOperatorAsFirstStep() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => . / ont:property;\n");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testUnnecessaryBaseOperatorInFilter() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => /ont:ClassA[. / rdf:type == /ont:ClassA];\n");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testUnnecessaryBaseOperatorNoWarningInSignatureArgument() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => CALL(.);\n");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testUnnecessaryBaseOperatorNoWarningWhenHasFilter() {
        String content = withPrefixes("queries: |\n" +
                "   DEFINE QUERY query => . [rdf:type == /ont:ClassA];\n");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testUnnecessaryBaseOperatorNoWarningWhenOnlyStep() {
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
}
