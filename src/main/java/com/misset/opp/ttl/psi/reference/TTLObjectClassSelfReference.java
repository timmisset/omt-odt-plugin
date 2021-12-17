package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.ttl.psi.TTLPrefixedName;
import org.jetbrains.annotations.NotNull;

public class TTLObjectClassSelfReference extends PsiReferenceBase.Poly<TTLPrefixedName> implements PsiPolyVariantReference {
    public TTLObjectClassSelfReference(TTLPrefixedName prefixedName, TextRange textRange) {
        super(prefixedName, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return PsiElementResolveResult.createResults(getElement());
    }
}
