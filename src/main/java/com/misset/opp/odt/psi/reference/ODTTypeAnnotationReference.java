package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.ODTMultiHostInjector;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;

public class ODTTypeAnnotationReference extends PsiReferenceBase.Poly<PsiComment> implements PsiPolyVariantReference {
    final TextRange textRange;
    public ODTTypeAnnotationReference(PsiComment psiElement, TextRange textRange) {
        super(psiElement, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ODTMultiHostInjector.resolveInOMT(myElement,
                OMTPrefixProvider.class,
                textRange.substring(myElement.getText()),
                OMTPrefixProvider::getPrefixMap)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
