package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTTypePrefixAnnotationReferenceTest extends OMTTestCase {

    @Test
    void testHasReferenceToPrefix() {
        String content = insideActivityWithPrefixes(
                "queries: |\n" +
                        "   /**\n" +
                        "    * @param $param (on<caret>t:SomeClass)\n" +
                        "    */\n" +
                        "   DEFINE QUERY query($param) => $param;\n"
        );
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof YAMLKeyValue));
    }

    @Test
    void testHandlesRename() {
        String content = insideActivityWithPrefixes(
                "queries: |\n" +
                        "   /**\n" +
                        "    * @param $param (on<caret>t:SomeClass)\n" +
                        "    */\n" +
                        "   DEFINE QUERY query($param) => /ont:SomeClass;\n" +
                        "   DEFINE QUERY query($param) => /ont:SomeClass;\n"
        );
        OMTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("oont");
            String text = omtFile.getText();
            Assertions.assertTrue(text.contains(" oont:"));
            Assertions.assertFalse(text.contains(" ont:"));
        });
    }

}
