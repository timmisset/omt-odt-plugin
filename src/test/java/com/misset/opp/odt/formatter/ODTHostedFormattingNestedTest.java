package com.misset.opp.odt.formatter;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.ODTFormattingTestCase;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ODTHostedFormattingNestedTest extends OMTTestCase {

    @Test
    void testIndentNested() {
        configureByText("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;");

        ODTFormattingTestCase.doFormat(myFixture, getProject(), getFile());
        String text = getEditor().getDocument().getText();
        Assertions.assertEquals("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;", text);
    }

    @Test
    void testContinuationIndentOnEnter() {
        OMTFile omtFile = configureByText("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA = /ont:Class / <caret>ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;");

        WriteCommandAction.runWriteCommandAction(getProject(), () -> myFixture.type("\n"));

        String text = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA = /ont:Class / \n" +
                "          ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;", text);
    }

    @Test
    void testContinuationIndentOnEnterNonLiteralBlock() {
        OMTFile omtFile = configureByText("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun:\n" +
                "      VAR $variableA = /ont:Class / <caret>ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;");

        WriteCommandAction.runWriteCommandAction(getProject(), () -> myFixture.type("\n"));

        String text = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun:\n" +
                "      VAR $variableA = /ont:Class / \n" +
                "          ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;", text);
    }

    @Test
    void testContinuationIndent() {
        OMTFile omtFile = configureByText("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA = /ont:Class / \n" +
                "      ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;");

        ODTFormattingTestCase.doFormat(myFixture, getProject(), getFile());

        String text = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("model:\n" +
                "  Procedure: !Procedure\n" +
                "    onRun: |\n" +
                "      VAR $variableA = /ont:Class /\n" +
                "          ont:property;\n" +
                "      VAR $variableB;\n" +
                "      VAR $variableC;", text);
    }

}
