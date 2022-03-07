package com.misset.opp.odt.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTDocumentationProvider extends AbstractDocumentationProvider implements DocumentationProvider {

    @Override
    public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (!(element instanceof ODTDocumented)) {
            return null;
        }
        return ((ODTDocumented) element).getDocumentation();
    }

    @Override
    public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement, int targetOffset) {
        if (contextElement == null) {
            return null;
        }
        PsiElement element = super.getCustomDocumentationElement(editor, file, contextElement, targetOffset);
        if (element == null && contextElement.getParent() instanceof ODTDocumented) {
            element = contextElement.getParent();
        }
        return element;
    }

    /**
     * Creates a key/value row for the rendered documentation.
     */
    public static void addKeyValueSection(String key, String value, StringBuilder sb) {
        sb.append(DocumentationMarkup.SECTION_HEADER_START);
        sb.append(key);
        sb.append(DocumentationMarkup.SECTION_SEPARATOR);
        sb.append(value);
        sb.append(DocumentationMarkup.SECTION_END);
    }

}
