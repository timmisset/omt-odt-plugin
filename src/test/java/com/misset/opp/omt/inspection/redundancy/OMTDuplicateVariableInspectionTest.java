package com.misset.opp.omt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTDuplicateVariableInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTDuplicateVariableInspection.class);
    }

    @Test
    void testActivityDuplicateInsideSameBlock() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "-  $test\n" +
                        "-  name: $test\n"
        );
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testActivityDuplicateInsideDifferentBlocks() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "-  $test\n" +
                        "params:\n" +
                        "-  name: $test\n"
        );
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testComponentDuplicateInsideDifferentBlocks() {
        String content = insideComponentWithPrefixes(
                "variables:\n" +
                        "-  $test\n" +
                        "bindings:\n" +
                        "   test: $test\n"
        );
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testComponentDuplicateInsideDifferentBlocksWithAssignment() {
        String content = insideComponentWithPrefixes(
                "variables:\n" +
                        "-  $test = 'hi'\n" +
                        "bindings:\n" +
                        "   test: $test\n"
        );
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testStandaloneQueryDuplicate() {
        String content = insideStandaloneQueryWithPrefixes(
                "params:\n" +
                        "-  $test\n" +
                        "base: $test\n");
        configureByText(content);
        assertHasWarning("Duplication");
    }

}
