package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;

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
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return element != variable &&
                element.sameNameAs(variable) &&
                element.isDeclaredVariable() &&
                PsiRelationshipUtil.canBeRelatedElement(element, variable);
    }
}
