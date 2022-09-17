package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.TTLDeclarePrefix;
import com.misset.opp.ttl.psi.TTLPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLPrefixReference extends PsiReferenceBase.Poly<TTLPrefix> implements PsiPolyVariantReference {
    public TTLPrefixReference(TTLPrefix prefix, TextRange textRange) {
        super(prefix, textRange, true);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        PsiFile containingFile = getElement().getContainingFile();
        return PsiTreeUtil.findChildrenOfType(containingFile, TTLDeclarePrefix.class)
                .stream()
                .map(TTLDeclarePrefix::getPrefix)
                .filter(prefix -> prefix.getText().equals(getElement().getText()))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public @Nullable TTLPrefix resolve() {
        return (TTLPrefix) super.resolve();
    }
}
