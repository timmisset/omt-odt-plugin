package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTParameterAnnotationReferenceTest extends ODTTestCase {

    @Test
    void testHasReferenceToParameter() {
        String content = "/**\n" +
                " * bla bla bla\n" +
                " * @param $pa<caret>ram (string)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => 'hello world';";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof ODTVariable));
    }

    @Test
    void testRefactorRename() {
        String content = "/**\n" +
                " * bla bla bla\n" +
                " * @param $pa<caret>ram (string)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => 'hello world';";
        ODTFileTestImpl odtFileTest = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(),
                () -> {
                    myFixture.renameElementAtCaret("$newParameterName");
                    Assertions.assertEquals("/**\n" +
                            " * bla bla bla\n" +
                            " * @param $newParameterName (string)\n" +
                            " */\n" +
                            "DEFINE QUERY query($newParameterName) => 'hello world';", odtFileTest.getText());
                });
    }

}
