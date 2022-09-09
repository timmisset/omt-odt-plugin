package com.misset.opp.testCase;

import com.google.common.base.Strings;
import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.odt.psi.ODTFile;
import org.junit.jupiter.api.Assertions;

public class ODTFormattingTestCase extends ODTTestCase {

    private static final int INDENT_SIZE = 4;

    protected void enter() {
        WriteCommandAction.runWriteCommandAction(getProject(), () -> myFixture.type("\n"));
    }

    public static void doFormat(JavaCodeInsightTestFixture myFixture, Project project, PsiFile file) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(myFixture.getProject());
        WriteCommandAction.runWriteCommandAction(project, (Computable<PsiElement>) () -> codeStyleManager.reformat(file));
    }

    protected void format() {
        doFormat(myFixture, getProject(), getFile());
    }

    @Override
    protected ODTFileTestImpl configureByText(String content) {
        // todo: make indentation a setting
        ODTFile odtFile = super.configureByText(content, true);
        CodeStyleSettings settings = CodeStyle.getSettings(odtFile);
        CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();
        indentOptions.INDENT_SIZE = INDENT_SIZE;
        indentOptions.CONTINUATION_INDENT_SIZE = INDENT_SIZE;

        return (ODTFileTestImpl) odtFile;
    }

    protected String getDocumentText() {
        return getEditor().getDocument().getText();
    }

    protected String getIndent() {
        return Strings.repeat(" ", INDENT_SIZE);
    }

    protected String getIndentedText(String text) {
        return text.replace("<indent>", getIndent());
    }

    protected void assertFormatting(String before, String after) {
        configureByText(before);
        format();
        Assertions.assertEquals(after, getDocumentText());
    }
}
