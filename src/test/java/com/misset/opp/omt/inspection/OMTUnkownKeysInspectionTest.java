package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnkownKeysInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnkownKeysInspection.class);
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
