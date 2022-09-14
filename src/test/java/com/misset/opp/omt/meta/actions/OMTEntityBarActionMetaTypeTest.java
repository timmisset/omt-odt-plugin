package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTEntityBarActionMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnkownKeysInspection.class));
    }

    @Test
    void testValidEntityBarActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   entitybar:\n" +
                "   - icon: icon\n");
        inspection.assertNoErrors();
    }

    @Test
    void testInvalidEntityBarActionProperty() {
        configureByText("test.module.omt", "actions:\n" +
                "   entitybar:\n" +
                "   - title: Title\n" +
                "     icon: icon\n");
        inspection.assertHasError("Key 'title' is not expected here");
    }

}
