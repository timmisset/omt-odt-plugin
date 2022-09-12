package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Optional;

public abstract class OMTYamlPlainTextDelegateAbstract extends YAMLPlainTextImpl {
    protected OMTYamlPlainTextDelegateAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return Optional.ofNullable(getReference())
                .stream()
                .toArray(PsiReference[]::new);
    }
}
