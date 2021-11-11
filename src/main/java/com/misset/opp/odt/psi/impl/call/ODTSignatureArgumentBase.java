package com.misset.opp.odt.psi.impl.call;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTSignatureArgumentBase extends ASTWrapperPsiElement implements ODTSignatureArgument {
    public ODTSignatureArgumentBase(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        return Optional.ofNullable(getResolvableValue())
                .map(Resolvable::resolve)
                .or(this::resolveCommandBlock)
                .orElse(Collections.emptySet());
    }

    private Optional<Set<OntResource>> resolveCommandBlock() {
        // todo: try to resolve the command block
        return Optional.of(Collections.emptySet());
    }
}
