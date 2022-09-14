package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTMergeValidationMetaTypeTest extends OMTTestCase {
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class));
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
        inspection.assertHasError("Missing required key(s): 'query'");
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
        inspection.assertHasError("Missing required key(s): 'context'");
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
        inspection.assertNoErrors();
    }
}
