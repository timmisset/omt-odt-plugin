package com.misset.opp.omt.inspection;

import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.InspectionTestCase;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertHasWarning("Key 'titlee' is not expected here");
    }

    @Test
    void inspectKnownRootKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: titel\n" +
                "";
        configureByText(content);
        assertNoWarnings();
    }

}
