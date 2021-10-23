package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.OMTValueInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

class OMTMergeValidationTypeTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void testMergeValidationMissingRequiredKeysQuery() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeValidation\n" +
                        "   context: current\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'query'");
    }

    @Test
    void testMergeValidationMissingRequiredKeysContext() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeValidation\n" +
                        "   query: /a\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'context'");
    }

    @Test
    void testMergeValidationAccepted() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeValidation\n" +
                        "   context: current\n" +
                        "   query: /a:b\n" +
                        ""
        );
        configureByText(content);
        assertNoErrors();
    }
}
