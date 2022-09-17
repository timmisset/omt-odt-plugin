package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.ttl.psi.TTLDeclarePrefix;
import com.misset.opp.ttl.psi.TTLTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class TTLDeclarePrefixAbstract extends ASTWrapperPsiElement implements TTLDeclarePrefix {
    private static final TokenSet IRIREF = TokenSet.create(TTLTypes.IRIREF);

    protected TTLDeclarePrefixAbstract(@NotNull ASTNode node) {
        super(node);
    }

    private String getDeclaredNamespace() {
        return Arrays.stream(getNode().getChildren(IRIREF))
                .map(ASTNode::getText)
                .map(s -> s.substring(1, s.length() - 1))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getQualifiedIri(String localname) {
        String declaredNamespace = getDeclaredNamespace();
        return declaredNamespace == null ? null : (declaredNamespace + localname);
    }
}
