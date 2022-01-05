package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTBaseResolvable extends ODTASTWrapperPsiElement implements ODTResolvable {


    public ODTBaseResolvable(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Collections.emptySet();
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }

    @Override
    public String getDocumentation() {
        return null;
    }
}
