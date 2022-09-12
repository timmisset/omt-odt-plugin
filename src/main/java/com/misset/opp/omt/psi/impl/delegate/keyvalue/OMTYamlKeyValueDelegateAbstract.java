package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Optional;

public abstract class OMTYamlKeyValueDelegateAbstract extends YAMLKeyValueImpl {
    protected OMTYamlKeyValueDelegateAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return Optional.ofNullable(getReference())
                .stream()
                .toArray(PsiReference[]::new);
    }
}
