package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableAbstract extends ASTWrapperPsiElement implements ODTResolvable {

    protected ODTResolvableAbstract(@NotNull ASTNode node) {
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
    public String getDocumentation(Project project) {
        return null;
    }
}
