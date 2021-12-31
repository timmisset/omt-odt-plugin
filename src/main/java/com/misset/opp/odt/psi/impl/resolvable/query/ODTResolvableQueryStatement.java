package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.psi.ODTQueryStatement;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableQueryStatement extends ODTASTWrapperPsiElement implements ODTResolvable, ODTQueryStatement {
    public ODTResolvableQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

    @Override
    public @NotNull Set<OntResource> resolve(Set<OntResource> inputResources, PsiCall call) {
        return getQuery().resolve(inputResources, call);
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }
}
