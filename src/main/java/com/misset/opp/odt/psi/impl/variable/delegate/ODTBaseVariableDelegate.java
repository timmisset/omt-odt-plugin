package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.yaml.psi.YAMLValue;

public abstract class ODTBaseVariableDelegate implements ODTVariableDelegate {
    protected final ODTVariable element;

    public ODTBaseVariableDelegate(ODTVariable element) {
        this.element = element;
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

    protected PsiVariable getWrapper(PsiElement element) {
        if (element instanceof PsiVariable) {
            return (PsiVariable) element;
        } else if (element instanceof YAMLValue) {
            return (PsiVariable) OMTYamlDelegateFactory.createDelegate((YAMLValue) element);
        }
        return null;
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

}
