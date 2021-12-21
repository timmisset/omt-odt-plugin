package com.misset.opp.odt.psi.impl.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTBaseDefineQueryStatement extends ODTDefineStatement implements ODTDefineQueryStatement, ODTResolvable {
    public ODTBaseDefineQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public String getCallId() {
        return getName();
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

    @Override
    public @NotNull Set<OntResource> resolve(Set<OntResource> inputResources,
                                             PsiCall call) {
        decorateCall(call);
        return getQuery().resolve(inputResources, call);
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }

    @Override
    public String getType() {
        return "DEFINE QUERY";
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return true;
    }

    public @NotNull Set<OntResource> getBase() {
        return Optional.ofNullable(ODTDocumentationUtil.getJavaDocComment(this))
                .map(psiDocComment -> psiDocComment.findTagByName("base"))
                .map(docTag -> ODTDocumentationUtil.getTypeFromDocTag(docTag, 0))
                .orElse(Collections.emptySet());
    }

    @Override
    public boolean isStatic() {
        return true;
    }


}
