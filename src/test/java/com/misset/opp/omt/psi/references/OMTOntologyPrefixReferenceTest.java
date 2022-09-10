package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTOntologyPrefixReferenceTest extends OMTTestCase {

    @Test
    void testHasReferenceToPrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   DummyOntology: !Ontology\n" +
                "       prefix: a<caret>bc\n" +
                "\n";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
            Assertions.assertEquals("<http://abc.com>", ((YAMLKeyValue) elementAtCaret).getValueText());
        });
    }

    @Test
    void testRenamesPrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   DummyOntology: !Ontology\n" +
                "       prefix: a<caret>bc\n" +
                "\n";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("abcd");
            Assertions.assertEquals("prefixes:\n" +
                            "   abcd: <http://abc.com>\n" +
                            "\n" +
                            "model:\n" +
                            "   DummyOntology: !Ontology\n" +
                            "       prefix: abcd\n" +
                            "\n",
                    getEditor().getDocument().getText());
        });
    }

}
