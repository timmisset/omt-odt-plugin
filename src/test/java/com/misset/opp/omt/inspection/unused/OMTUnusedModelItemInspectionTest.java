package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedModelItemInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedModelItemInspection.class);
    }

    @Test
    void testUnusedModelItem() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title:\n" +
                "";
        configureByText(content);
        assertHasWarning("MyActivity is never used");
    }

    @Test
    void testUnusedModelItemNoWarningForComponent() {
        String content = "model:\n" +
                "   MyComponent: !Component\n" +
                "       title:\n" +
                "";
        configureByText(content);
        assertNoWarning("MyComponent is never used");
    }


}
