package com.misset.opp.omt.meta;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

class OMTMetaTypeTest extends OMTTestCase {

    @Test
    void testBuildInsertionSuffixMarkupMap() {
        String content = "mod<caret>";
        OMTFile omtFile = configureByText(content);
        setYamlIndentation(omtFile);
        myFixture.completeBasic();
        String textAfterCompletion = ReadAction.compute(omtFile::getText);
        assertEquals("model:\n" +
                "  ", textAfterCompletion);
    }

    @Test
    void testBuildInsertionSuffixMarkupSequence() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       param<caret>";
        OMTFile omtFile = configureByText(content);
        setYamlIndentation(omtFile);
        myFixture.completeBasic();
        String textAfterCompletion = ReadAction.compute(omtFile::getText);
        assertEquals("model:\n" +
                "   MyActivity: !Activity\n" +
                "       params:\n" +
                "       - ", textAfterCompletion);
    }

}
