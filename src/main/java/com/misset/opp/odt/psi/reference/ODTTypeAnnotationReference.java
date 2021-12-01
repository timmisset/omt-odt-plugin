package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;

public class ODTTypeAnnotationReference extends PsiReferenceBase.Poly<PsiElement> implements PsiPolyVariantReference {
    final TextRange textRange;

    public ODTTypeAnnotationReference(PsiElement psiElement,
                                      TextRange textRange) {
        super(psiElement, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final ODTFile containingFile = (ODTFile) myElement.getContainingFile();
        return containingFile.resolveInOMT(OMTPrefixProvider.class,
                        OMTPrefixProvider.KEY,
                        textRange.substring(myElement.getText()),
                        OMTPrefixProvider::getPrefixMap)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
