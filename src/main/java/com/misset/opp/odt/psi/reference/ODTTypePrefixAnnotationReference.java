package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;

public class ODTTypePrefixAnnotationReference extends ODTPolyReferenceBase<PsiElement> {
    final TextRange textRange;

    public ODTTypePrefixAnnotationReference(PsiElement psiElement,
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
                .map(this::toResults)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
