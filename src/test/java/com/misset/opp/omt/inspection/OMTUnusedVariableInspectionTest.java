package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.unused.OMTUnusedVariableInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedVariableInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedVariableInspection.class);
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
