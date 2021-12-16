package com.misset.opp.ttl.psi.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.prefix.TTLPrefixedNameHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TTLBaseIri extends ASTWrapperPsiElement implements TTLIri {
    public TTLBaseIri(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getQualifiedUri() {
        return Optional.ofNullable(getPrefixedName())
                .map(TTLPrefixedNameHolder::getQualifiedUri)
                .orElse(getTrimmedIriRef());
    }

    private String getTrimmedIriRef() {
        if (getPrefixedName() != null) {
            return null;
        }
        String iri = getFirstChild().getText();
        return iri.substring(1, iri.length() - 1);
    }
}
