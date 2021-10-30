package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ODTParameterAnnotationReference extends PsiReferenceBase.Poly<PsiComment> implements PsiPolyVariantReference {
    final String typeNotation;
    final TextRange textRange;

    public ODTParameterAnnotationReference(PsiComment psiElement,
                                           TextRange textRange,
                                           String typeNotation) {
        super(psiElement, textRange, false);
        this.textRange = textRange;
        this.typeNotation = typeNotation;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String name = textRange.substring(myElement.getText());
        final PsiElement nextVisibleLeaf = PsiTreeUtil.nextVisibleLeaf(myElement);
        if (nextVisibleLeaf != null) {
            final ODTDefineStatement defineStatement = PsiTreeUtil.getParentOfType(nextVisibleLeaf,
                    ODTDefineStatement.class);
            if (defineStatement != null) {
                final List<ODTVariable> odtVariables = Optional.ofNullable(defineStatement.getDefineParam())
                        .map(ODTDefineParam::getVariableList)
                        .orElse(Collections.emptyList());
                return PsiElementResolveResult
                        .createResults(odtVariables
                                .stream()
                                .filter(variable -> name.equals(variable.getName()))
                                .findFirst().orElse(null));
            }
        }
        return new ResolveResult[0];
    }
}
