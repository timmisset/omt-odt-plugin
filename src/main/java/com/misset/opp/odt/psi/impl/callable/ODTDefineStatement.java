package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class ODTDefineStatement extends ODTASTWrapperPsiElement implements PsiCallable, PsiJavaDocumentedElement {
    public ODTDefineStatement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    abstract public ODTDefineName getDefineName();

    @Override
    public @Nullable PsiDocComment getDocComment() {
        final PsiElement docEnd = PsiTreeUtil.prevVisibleLeaf(this);
        if(docEnd != null && docEnd.getParent() instanceof PsiDocComment) {
            return (PsiDocComment) docEnd.getParent();
        }
        return null;
    }

    @Override
    public String getDescription(String context) {
        return ODTDocumentationUtil.getJavaDocComment(this);
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 0;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    public String getName() {
        return getDefineName().getText();
    }

    public abstract ODTDefineParam getDefineParam();

    protected void decorateCall(Call call) {
        final List<ODTVariable> variables = Optional.ofNullable(getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        for (int i = 0; i < variables.size(); i++) {
            call.setParamType(variables.get(i).getName(), call.resolveSignatureArgument(i));
        }
    }

    @Override
    public PsiElement getCallTarget() {
        return getDefineName();
    }
}
