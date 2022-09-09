package com.misset.opp.odt.completion;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.odt.psi.ODTFile;
import org.junit.jupiter.api.Test;

class ODTTypedHandlerDelegateTest extends ODTTestCase {

    @Test
    void testIncludesTrailingSpaceAfterSlash() {
        String content = "$variable <caret>";
        ODTFile odtFile = configureByText(content);

        typeSlash(odtFile);

        assertEquals("$variable / ", odtFile.getText());
    }

    @Test
    void testDoesntIncludesTrailingSpaceAtStartOfStatement() {
        String content = "$variable = <caret>";
        ODTFile odtFile = configureByText(content, true);

        typeSlash(odtFile);

        assertEquals("$variable = /", odtFile.getText());
    }

    @Test
    void testDoesntIncludesTrailingSpaceInsideCommandArgument() {
        String content = "@COMMAND(<caret>";
        ODTFile odtFile = configureByText(content, true);

        typeSlash(odtFile);

        assertEquals("@COMMAND(/", odtFile.getText());
    }

    @Test
    void testDoesntIncludesTrailingSpaceAtStartOfScriptline() {
        String content = "DEFINE COMMAND command => { <caret> }";
        ODTFile odtFile = configureByText(content, true);

        typeSlash(odtFile);

        assertEquals("DEFINE COMMAND command => { / }", odtFile.getText());
    }

    @Test
    void testDoesntIncludesTrailingSpaceInsideFilter() {
        String content = "$variable[<caret>";
        ODTFile odtFile = configureByText(content, true);

        typeSlash(odtFile);

        assertEquals("$variable[/", odtFile.getText());
    }

    private void typeSlash(PsiFile file) {
        myFixture.type("/");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
            Document document = myFixture.getDocument(file);
            documentManager.doPostponedOperationsAndUnblockDocument(document);
            documentManager.commitDocument(document);
        });

    }

}
