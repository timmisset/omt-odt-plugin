package com.misset.opp.omt.inspection;

import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTDuplicateVariableInspectionTest extends InspectionTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(OMTDuplicateVariableInspection.class);
    }

    @Test
    void testActivityDuplicateInsideSameBlock() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "-  $test\n" +
                        "-  name: $test\n"
        );
        configureByText(content);
        assertHasError("Duplication");
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
        assertHasError("Duplication");
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
        assertHasError("Duplication");
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
        assertHasError("Duplication");
    }

    @Test
    void testStandaloneQueryDuplicate() {
        String content = insideStandaloneQueryWithPrefixes(
                "params:\n" +
                        "-  $test\n" +
                        "base: $test\n");
        configureByText(content);
        assertHasError("Duplication");
    }

}
