package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedInspectionTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedInspection.class);
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

    @Test
    void testOMTVariableUnused() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "\n"
        );
        configureByText(content);
        assertHasWarning("$test is never used");
    }

    @Test
    void testOMTVariableUsed() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "onStart: |\n" +
                        "  @LOG($test);\n" +
                        ""
        );
        configureByText(content);
        assertNoWarning("$test is never used");
    }

}
