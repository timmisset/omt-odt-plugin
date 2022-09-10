package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTTypePrefixAnnotationReferenceTest extends ODTTestCase {

    @Test
    void testHasReferenceToPrefix() {
        String content =
                "PREFIX ont: <http://ontology#>;\n" +
                        "/**\n" +
                        " * @param $param (on<caret>t:SomeClass)\n" +
                        "*/\n" +
                        "DEFINE QUERY query($param) => $param;\n";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof ODTDefinePrefix));
    }

    @Test
    void testHandlesRename() {
        String content =
                "PREFIX ont: <http://ontology#>;" +
                        "/**\n" +
                        " * @param $param (on<caret>t:SomeClass)\n" +
                        "*/\n" +
                        "DEFINE QUERY query($param) => /ont:SomeClass;\n" +
                        "DEFINE QUERY query($param) => /ont:SomeClass;\n";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("oont");
            assertEquals(
                    "PREFIX oont: <http://ontology#>;" +
                            "/**\n" +
                            " * @param $param (oont:SomeClass)\n" +
                            "*/\n" +
                            "DEFINE QUERY query($param) => /oont:SomeClass;\n" +
                            "DEFINE QUERY query($param) => /oont:SomeClass;\n", getFile().getText());
        });
    }

}
