package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTNamespacePrefixReferenceTest extends OMTTestCase {

    @Test
    void testPrefixResolvesToODTDefinePrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       onStart: |\n" +
                "           PREFIX abc: <http://abc.com>\n" +
                "           @LOG(/a<caret>bc:test);\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof ODTDefinePrefix));
    }

    @Test
    void testPrefixResolvesToOMTPrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @LOG(/a<caret>bc:test);\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof YAMLKeyValue));
    }


    @Test
    void testPrefixResolvesToOMTModelItemPrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       prefixes:\n" +
                "           abc: <http://def.com>\n" +
                "       onStart: |\n" +
                "           @LOG(/a<caret>bc:test);\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement keyValue = myFixture.getElementAtCaret();
            Assertions.assertTrue(keyValue instanceof YAMLKeyValue);
            Assertions.assertEquals("<http://def.com>", ((YAMLKeyValue) keyValue).getValueText());
        });
    }

    @Test
    void testPrefixRenamesOMTPrefix() {
        String content = "prefixes:\n" +
                "   abc: <http://abc.com>\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @LOG(/a<caret>bc:test);\n" +
                "";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            // rename the prefix using the references
            myFixture.renameElementAtCaret("abcd");

            Assertions.assertEquals("prefixes:\n" +
                            "   abcd: <http://abc.com>\n" +
                            "\n" +
                            "model:\n" +
                            "   MyActivity: !Activity\n" +
                            "       onStart: |\n" +
                            "           @LOG(/abcd:test);\n",
                    getContainingOMTFile((ODTFileImpl) getFile()).getText());
        });
    }
}
