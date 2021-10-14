package com.misset.opp.omt.inspection;

import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTUnkownKeysInspectionTest extends InspectionTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(OMTUnkownKeysInspection.class);
    }

    @Test
    void inspectUnknownRootKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       titlee: titel\n" +
                "";
        configureByText(content);
        assertHasError("Key 'titlee' is not expected here");
    }

    @Test
    void inspectKnownRootKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: titel\n" +
                "";
        configureByText(content);
        assertNoErrors();
    }

}
