package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.inspection.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.OMTValueInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTMergeListsTest extends InspectionTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(OMTMissingKeysInspection.class);
        myFixture.enableInspections(OMTUnkownKeysInspection.class);
        myFixture.enableInspections(OMTValueInspection.class);
    }

    @Test
    void testMergeListMissingRequiredKeys() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   when: b\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'predicates, subjects'");
    }

    @Test
    void testMergeListAccepted() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   predicates: /a:b\n" +
                        "   subjects: /a:b\n" +
                        "   when: b\n" +
                        ""
        );
        configureByText(content);
        assertNoErrors();
    }
}
