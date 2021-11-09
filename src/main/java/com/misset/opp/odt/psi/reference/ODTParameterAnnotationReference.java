package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.javadoc.PsiDocTag;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ODTParameterAnnotationReference extends PsiReferenceBase.Poly<PsiDocTag> implements PsiPolyVariantReference {
    public ODTParameterAnnotationReference(PsiDocTag psiDogTag, TextRange textRange) {
        super(psiDogTag, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final PsiJavaDocumentedElement owner = myElement.getContainingComment().getOwner();
        String variableName = Optional.ofNullable(myElement.getValueElement()).map(PsiElement::getText).orElse("");
        if(owner instanceof ODTDefineStatement) {
            ODTDefineStatement defineStatement = (ODTDefineStatement) owner;
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
        return ResolveResult.EMPTY_ARRAY;
    }
}
