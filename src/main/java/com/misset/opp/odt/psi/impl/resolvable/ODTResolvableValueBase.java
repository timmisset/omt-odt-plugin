package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableValueBase extends ODTASTWrapperPsiElement implements ODTResolvableValue {
    public ODTResolvableValueBase(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        if (getQuery() != null) {
            return getQuery().resolve();
        }
        if (getCommandCall() != null) {
            return getCommandCall().resolve();
        }
        return Collections.emptySet();
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }

    public ODTResolvable getResolvable() {
        return Optional.ofNullable(getQuery())
                .map(ODTResolvable.class::cast)
                .orElse(getCommandCall());
    }

    @Override
    public boolean isMultiple() {
        return Optional.ofNullable(getResolvable()).map(Resolvable::isMultiple).orElse(false);
    }
}
