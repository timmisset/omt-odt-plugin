package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.psi.OMTVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTVariableReference extends PsiReferenceBase<YAMLValue> implements PsiPolyVariantReference {
    public OMTVariableReference(@NotNull YAMLValue element, @NotNull TextRange rangeInElement) {
        super(element, rangeInElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return new ResolveResult[0];
    }

    @Override
    public @Nullable PsiElement resolve() {
        return null;
    }
}
