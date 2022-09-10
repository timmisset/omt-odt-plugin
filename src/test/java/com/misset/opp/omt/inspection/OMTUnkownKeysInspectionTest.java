package com.misset.opp.omt.inspection;

import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTUnkownKeysInspectionTest extends OMTTestCase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnkownKeysInspection.class));
    }

    @Test
    void inspectUnknownRootKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       titlee: titel\n" +
                "";
        configureByText(content);
        inspection.assertHasError("Key 'titlee' is not expected here");
    }

    @Test
    void inspectKnownRootKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: titel\n" +
                "";
        configureByText(content);
        inspection.assertNoErrors();
    }

}
