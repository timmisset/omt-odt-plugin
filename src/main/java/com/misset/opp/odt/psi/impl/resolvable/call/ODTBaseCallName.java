package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.util.ODTResolvableUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTBaseCallName extends ODTASTWrapperPsiElement implements
        ODTCallName,
        ODTDocumented,
        ODTResolvable {
    public ODTBaseCallName(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public ODTCall getParent() {
        return (ODTCall) super.getParent();
    }

    @Override
    public String getDocumentation() {
        ODTCall call = getParent();
        Callable callable = call.getCallable();
        if (callable == null) {
            return null;
        }
        return callable.getDescription(call.getLocalCommandProvider());
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        ODTResolvableUtil.annotateResolved(resolve(), holder, this, false);
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getParent().resolve();
    }

    @Override
    public PsiElement getDocumentationElement() {
        return this;
    }

}
