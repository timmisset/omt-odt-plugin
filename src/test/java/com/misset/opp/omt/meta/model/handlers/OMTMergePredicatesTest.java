package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.inspection.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.OMTValueInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.misset.opp.omt.meta.model.handlers.OMTMergePredicatesType.USE_IS_ONLY_AVAILABLE;
import static com.misset.opp.omt.meta.model.handlers.OMTMergePredicatesType.USE_IS_REQUIRED;

class OMTMergePredicatesTest extends InspectionTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(OMTMissingKeysInspection.class);
        myFixture.enableInspections(OMTUnkownKeysInspection.class);
        myFixture.enableInspections(OMTValueInspection.class);
    }

    @Test
    void testMergeHandlerMissingUseWhenFromIsBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a:b\n" +
                        "   predicates: /a:b\n" +
                        "   from: both\n" +
                        ""
        );
        configureByText(content);
        assertHasError(USE_IS_REQUIRED);
    }

    @Test
    void testMergeHandlerNotMissingUseWhenFromIsBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a:b\n" +
                        "   predicates: /a:b\n" +
                        "   from: both\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertNoErrors();
    }

    @Test
    void testMergeHandlerUseWhenFromIsNotBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a\n" +
                        "   predicates: /b\n" +
                        "   from: current\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertHasError(USE_IS_ONLY_AVAILABLE);
    }

    @Test
    void testMergeHandlerMissingRequiredFields() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   from: both\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'predicates, subjects'");
    }
}
