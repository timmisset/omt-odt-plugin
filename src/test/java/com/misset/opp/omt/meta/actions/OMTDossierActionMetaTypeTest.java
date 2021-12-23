package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTDossierActionMetaTypeTest extends OMTInspectionTestCase {
    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnkownKeysInspection.class);
    }

    @Test
    void testValidDossierActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   dossier:\n" +
                "   - title: Title\n" +
                "     icon: icon\n" +
                "     params:\n" +
                "     - $paramA\n");
        assertNoErrors();
    }

    @Test
    void testInvalidDossierActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   dossier:\n" +
                "   - title: Title\n" +
                "     icon: icon\n" +
                "     disabled: true\n" +
                "     params:\n" +
                "     - $paramA\n");
        assertHasError("Key 'disabled' is not expected here");
    }

}