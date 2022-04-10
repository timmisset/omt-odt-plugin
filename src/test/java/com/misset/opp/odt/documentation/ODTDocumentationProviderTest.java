package com.misset.opp.odt.documentation;

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ODTDocumentationProviderTest extends OMTTestCase {

    ODTDocumentationProvider documentationProvider;

    @BeforeEach
    void setUpProvider() {
        documentationProvider = new ODTDocumentationProvider();
    }

    @Test
    void testGenerateDocReturnsNullForNonODTDocumentedType() {
        Assertions.assertNull(new ODTDocumentationProvider().generateDoc(null, null));
    }

    @Test
    void testGetCustomDocumentationElementReturnsNull() {
        Assertions.assertNull(new ODTDocumentationProvider().getCustomDocumentationElement(
                mock(Editor.class),
                mock(PsiFile.class),
                null,
                0));
    }

    @Test
    void testGenerateDocReturnsDocumentation() {
        String content = "commands:\n" +
                "   /**\n" +
                "    * Some information about this command\n" +
                "    */\n" +
                "   DEFINE COMMAND command($param) => { @LOG($param); }\n" +
                "   DEFINE COMMAND commandB => { @<caret>command(); }";
        configureByText(content);
        Assertions.assertTrue(getDocumentationAtCaret().contains("Some information about this command"));
    }

    @Test
    void testGenerateDocReturnsNullForUndocumented() {
        String content = "commands:\n" +
                "   DEFINE COMMAND commandB => { @<caret>UNKNOWN(); }";
        configureByText(content);
        Assertions.assertNull(getDocumentationAtCaret());
    }

    @Test
    void testGenerateDocReturnsOMTModelItemDocumentation() {
        OMTOntologyTestCase.initOntologyModel();
        String content = ("model:\n" +
                "   Activity: !Activity\n" +
                "       params:\n" +
                "       - $paramA (string)\n" +
                "" +
                "   Activity2: !Activity\n" +
                "       onStart: |\n" +
                "           @<caret>Activity('test');\n" +
                "");
        configureByText(content);
        Assertions.assertTrue(findDocumentationAtCaret().contains("Type:"));
        Assertions.assertTrue(findDocumentationAtCaret().contains("Params:"));
    }

    /**
     * Returns the documentation by resolving a reference at the caret to the declaring element
     */
    private String getDocumentationAtCaret() {
        return ReadAction.compute(() -> {
            final PsiElement originalElement = myFixture.getElementAtCaret();
            return getDocumentationForElement(originalElement);
        });
    }

    /**
     * Returns the documentation for the actual element at the caret
     */
    private String findDocumentationAtCaret() {
        return ReadAction.compute(() -> {
            PsiElement elementAt = getFile().findElementAt(myFixture.getCaretOffset());
            return getDocumentationForElement(elementAt);
        });
    }

    private String getDocumentationForElement(PsiElement originalElement) {
        PsiElement element = DocumentationManager
                .getInstance(getProject())
                .findTargetElement(myFixture.getEditor(), originalElement.getContainingFile(), originalElement);

        if (element == null) {
            element = originalElement;
        }

        final DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(element);
        return documentationProvider.generateDoc(element, originalElement);
    }

}
