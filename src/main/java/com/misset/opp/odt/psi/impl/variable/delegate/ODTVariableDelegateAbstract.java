package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import com.misset.opp.resolvable.Variable;

import java.util.Optional;

public abstract class ODTVariableDelegateAbstract implements ODTVariableDelegate {
    protected final ODTVariable element;

    protected ODTVariableDelegateAbstract(ODTVariable element) {
        this.element = element;
    }

    @Override
    public ODTVariable getElement() {
        return element;
    }

    protected boolean isAssignmentPart() {
        return PsiTreeUtil.getParentOfType(element,
                ODTVariableAssignment.class,
                ODTVariableValue.class) instanceof ODTVariableValue;
    }

    @Override
    public boolean isAssignedVariable() {
        return false;
    }

    @Override
    public Variable getDeclared() {
        return element;
    }

    @Override
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return element != variable &&
                element.sameNameAs(variable) &&
                element.isDeclaredVariable() &&
                PsiRelationshipUtil.canBeRelatedElement(element, variable);
    }

    @Override
    public void delete() {

    }

    @Override
    public String getSource() {
        return "ODT variable";
    }

    protected Optional<Variable> getFromFile() {
        PsiFile containingFile = element.getContainingFile();
        if (containingFile instanceof ODTFile) {
            return ((ODTFile) containingFile).getVariables(element.getName()).stream().findFirst();
        }
        return Optional.empty();
    }
}
