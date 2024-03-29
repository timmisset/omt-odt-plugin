package com.misset.opp.odt.psi.impl.resolvable.callable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTDefineQueryStatementAbstract extends ODTDefineStatementAbstract implements ODTDefineQueryStatement {
    protected ODTDefineQueryStatementAbstract(@NotNull ASTNode node) {
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
        return Optional.ofNullable(getReturnType())
                .orElseGet(() -> getQuery().resolve());
    }

    @Override
    public @NotNull Set<OntResource> resolve(@Nullable Context context) {
        PsiFile containingFile = getContainingFile();
        if (containingFile == null || context == null) {
            return Collections.emptySet();
        }

        context.getFilesInScope().add(containingFile);
        decorateCall(context.getCall());
        return Optional.ofNullable(getReturnType())
                .orElseGet(() -> getQuery().resolve(context));
    }

    @Override
    public CallableType getType() {
        return CallableType.DEFINE_QUERY;
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
    public boolean requiresInput() {
        return getQuery().requiresInput();
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Collections.emptySet();
    }
}
