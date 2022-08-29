package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocTag;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.misset.opp.odt.documentation.ODTDocumentationUtil.getDocOwner;

public class ODTParameterAnnotationReference extends PsiReferenceBase.Poly<PsiDocTag> implements
        PsiPolyVariantReference,
        EmptyResolveMessageProvider {

    public ODTParameterAnnotationReference(PsiDocTag psiDogTag, TextRange textRange) {
        super(psiDogTag, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final PsiElement owner = getDocOwner(myElement);
        if (owner instanceof ODTDefineStatement) {
            return resolveDefineParam((ODTDefineStatement) owner);
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    private ResolveResult @NotNull [] resolveDefineParam(ODTDefineStatement defineStatement) {
        String variableName = Optional.ofNullable(myElement.getValueElement()).map(PsiElement::getText).orElse("");
        final List<ODTVariable> odtVariables = Optional.ofNullable(defineStatement.getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        return odtVariables
                .stream()
                .filter(variable -> variableName.equals(variable.getName()))
                .findFirst()
                .map(PsiElementResolveResult::createResults)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        String name = Optional.ofNullable(getElement().getValueElement())
                .map(PsiElement::getText)
                .orElse("");
        return "Cannot resolve parameter '" + name + "'";
    }
}
