package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTUnusedInspectionTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTUnusedInspection.class);
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
