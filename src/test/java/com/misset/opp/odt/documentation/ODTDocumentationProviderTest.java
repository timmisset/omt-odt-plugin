package com.misset.opp.odt.documentation;

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ODTDocumentationProviderTest extends ODTTestCase {

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
        String content = "/**\n" +
                " * Some information about this command\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => { @LOG($param); }\n" +
                "DEFINE COMMAND commandB => { @<caret>command(); }";
        configureByText(content);
        Assertions.assertTrue(getDocumentationAtCaret().contains("Some information about this command"));
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
