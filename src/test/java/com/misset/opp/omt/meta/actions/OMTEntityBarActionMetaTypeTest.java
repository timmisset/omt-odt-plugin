package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTEntityBarActionMetaTypeTest extends OMTInspectionTestCase {
    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnkownKeysInspection.class);
    }

    @Test
    void testValidEntityBarActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   entitybar:\n" +
                "   - icon: icon\n");
        assertNoErrors();
    }

    @Test
    void testInvalidEntityBarActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   entitybar:\n" +
                "   - title: Title\n" +
                "     icon: icon\n");
        assertHasError("Key 'title' is not expected here");
    }

}