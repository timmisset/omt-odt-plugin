package com.misset.opp.odt.formatter;

import com.misset.opp.testCase.ODTFormattingTestCase;
import org.junit.jupiter.api.Test;

class ODTFormattingSpacingTest extends ODTFormattingTestCase {

    @Test
    void testLineFeedBetweenScriptLines() {
        assertFormatting("VAR $variableA;VAR $variableB;",
                "VAR $variableA;\nVAR $variableB;");
    }

    @Test
    void testSpacingAroundQueryPathSeparator() {
        assertFormatting("VAR $variableA = /ont:Class/ont:somePath;",
                "VAR $variableA = /ont:Class / ont:somePath;");
    }

}