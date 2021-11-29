package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;

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
                validateCommonParent(variable);
    }

    private boolean validateCommonParent(ODTVariable usage) {
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(element, usage);
        // DEFINE COMMAND command($variable) => { $variable } <-- same common parent as ODTDefineStatement OK
        // DEFINE COMMAND command => { VAR $variable; @LOG($variable); } <-- common parent will be script, so not OK (yet)
        if (commonParent instanceof ODTDefineStatement) {
            return true;
        }

        if (commonParent instanceof ODTScript) {
            // only when the declared variable is in the root of the common parent:
            // VAR $variable
            // IF true { @LOG($variable); } <-- passed

            // IF true { VAR $variable; } ELSE { @LOG($variable); } <-- failed
            return PsiTreeUtil.getParentOfType(element, ODTDefineStatement.class, ODTScript.class) == commonParent;
        }
        return false;
    }
}
