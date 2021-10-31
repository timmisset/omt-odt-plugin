package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.reference.ODTVariableReference;

import java.util.Optional;

import static com.misset.opp.util.CachingUtil.getCachedOrCalcute;
public class ODTVariableAssignmentDelegate extends ODTDeclaredVariableDelegate {

    public ODTVariableAssignmentDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return getCachedOrCalcute(element, IS_DECLARED_VARIABLE, () -> {
            if(isAssignmentPart()) { return false; }
            return isOMTVariableProvider() || Optional.ofNullable(element.getParent())
                    .map(PsiElement::getParent)
                    .map(ODTDeclareVariable.class::isInstance)
                    .orElse(Boolean.FALSE);
        });
    }

    @Override
    public PsiReference getReference() {
        return !isDeclaredVariable() ? new ODTVariableReference(element) : null;
    }
}
