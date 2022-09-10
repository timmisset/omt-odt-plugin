package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTNamespacePrefixReferenceTest extends ODTTestCase {

    @Test
    void testPrefixResolvesToODTDefinePrefix() {
        String content = "PREFIX abc: <http://abc.com>\n" +
                "@LOG(/a<caret>bc:test);";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof ODTDefinePrefix));
    }

    @Test
    void testPrefixResolvesToExternalPrefix() {
        String content = "@LOG(/a<caret>bc:test);";
        ODTFileTestImpl odtFileTest = configureByText(content);
        PsiPrefix prefix = mock(PsiPrefix.class);
        doReturn("abc").when(prefix).getName();
        doReturn(prefix).when(prefix).getOriginalElement();
        odtFileTest.addPrefix(prefix);
        ReadAction.run(() -> {
            ODTNamespacePrefix abc = myFixture.findElementByText("abc", ODTNamespacePrefix.class);
            Assertions.assertEquals(prefix, abc.getReference().resolve());
        });
    }

    @Test
    void testPrefixRenamesOMTPrefix() {
        String content = "PREFIX abc: <http://abc.com>\n" +
                "@LOG(/a<caret>bc:test);";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            // rename the prefix using the references
            myFixture.renameElementAtCaret("abcd");

            Assertions.assertEquals("PREFIX abcd: <http://abc.com>;\n" +
                            "@LOG(/abcd:test);",
                    getFile().getText());
        });
    }
}
