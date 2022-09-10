package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTDossierActionMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnkownKeysInspection.class));
    }

    @Test
    void testValidDossierActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   dossier:\n" +
                "   - title: Title\n" +
                "     icon: icon\n" +
                "     params:\n" +
                "     - $paramA\n");
        inspection.assertNoErrors();
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
        inspection.assertHasError("Key 'disabled' is not expected here");
    }

}
