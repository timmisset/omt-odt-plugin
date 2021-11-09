package com.misset.opp.odt.documentation;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTDocumentationProvider implements DocumentationProvider {

    @Override
    public @Nullable String generateHoverDoc(@NotNull PsiElement element,
                                             @Nullable PsiElement originalElement) {
        if(element instanceof ODTDocumented) {
            return ((ODTDocumented) element).getDocumentation();
        }
        return null;
    }
}
