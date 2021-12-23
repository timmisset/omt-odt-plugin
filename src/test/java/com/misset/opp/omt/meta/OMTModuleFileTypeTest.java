package com.misset.opp.omt.meta;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTModuleFileTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnkownKeysInspection.class);
    }

    @Test
    void testHasNoModelProperty() {
        configureByText("test.module.omt", "model:\n" +
                "   Activiteit: !Activity\n");
        assertHasError("Key 'model' is not expected here");
    }
}