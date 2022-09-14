package com.misset.opp.omt.inspection.unused;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTUnusedModelItemInspectionTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnusedModelItemInspection.class));
    }

    @Test
    void testUnusedModelItem() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title:\n" +
                "";
        configureByText(content);
        inspection.assertHasWarning("MyActivity is never used");
    }

    @Test
    void testUnusedModelItemNoWarningForComponent() {
        String content = "model:\n" +
                "   MyComponent: !Component\n" +
                "       title:\n" +
                "";
        configureByText(content);
        inspection.assertNoWarning("MyComponent is never used");
    }


}
