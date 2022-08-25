package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTImportedMemberRefReference extends OMTPlainTextReference {

    public OMTImportedMemberRefReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return resolveExportableMemberReference();
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        ResolveResult[] resolveResults = resolveExportableMemberReference(false);
        if (resolveResults.length == 0) {
            return false;
        }
        PsiElement result = resolveResults[0].getElement();
        return result != null && result.getOriginalElement() == element.getOriginalElement();
    }
}
