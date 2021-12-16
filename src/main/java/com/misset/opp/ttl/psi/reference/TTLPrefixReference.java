package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.TTLPrefixId;
import com.misset.opp.ttl.psi.TTLPrefixedName;
import org.jetbrains.annotations.NotNull;

public class TTLPrefixReference extends PsiReferenceBase.Poly<TTLPrefixedName> implements PsiPolyVariantReference {
    public TTLPrefixReference(TTLPrefixedName prefixedName, TextRange textRange) {
        super(prefixedName, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String prefixId = getElement().getPrefixId();
        PsiFile containingFile = getElement().getContainingFile();
        return PsiTreeUtil.findChildrenOfType(containingFile, TTLPrefixId.class)
                .stream()
                .filter(ttlPrefixId -> ttlPrefixId.getPrefixId().equals(prefixId))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }
}
