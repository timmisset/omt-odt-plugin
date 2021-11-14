package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableQuery extends ODTASTWrapperPsiElement implements ODTQuery, ODTResolvable {
    public ODTResolvableQuery(@NotNull ASTNode node) {
        super(node);
    }

    public Set<OntResource> resolveFromSet(Set<OntResource> fromSet) {
        return resolve();
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        /* Inspection is performed on the individual steps, not the entire query */
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        /* Annotation is performed on the individual steps, not the entire query */
    }
}
