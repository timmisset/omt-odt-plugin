package com.misset.opp.omt.inspection.unused;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTUnusedVariableInspectionTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnusedVariableInspection.class));
    }

    @Test
    void testOMTVariableUnused() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "\n"
        );
        configureByText(content);
        inspection.assertHasWarning("$test is never used");
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
        inspection.assertNoWarning("$test is never used");
    }

}
