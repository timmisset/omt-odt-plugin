package com.misset.opp.omt.psi.impl.prefixes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.prefixes.OMTPrefix;
import com.misset.opp.omt.psi.prefixes.OMTPrefixBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Optional;

public class OMTPrefixImpl extends YAMLKeyValueImpl implements OMTPrefix {
    public OMTPrefixImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isPrefix(String prefix) {
        return Optional.ofNullable(getKey())
                .map(PsiElement::getText)
                .map(s -> s.equals(prefix))
                .orElse(false);
    }
}
