package com.misset.opp.odt.psi.impl.variables.delegates;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;

public class ODTDeclaredVariableDelegate extends ODTBaseVariableDelegate {

    public ODTDeclaredVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return true;
    }

    @Override
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return element.sameNameAs(variable) && isAccessibleTo(variable);
    }

    @Override
    public PsiReference getReference() {
        return null;
    }

    private boolean isAccessibleTo(ODTVariable variableWrapper) {
        if(variableWrapper == null || !variableWrapper.isValid() || !element.isValid()) { return false; }
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(element, variableWrapper);
        if (commonParent instanceof ODTScript) {
            // a script block is the common parent, this means the variable is accessible only if this script is also
            // the first Script parent of the declared variable, i.e.
            // VAR $x
            // IF ... { $x } <-- $x has access to VAR $x
            //
            // {
            //    { VAR $x; }
            //    $x <-- $x has no access to VAR $x
            // }
            return PsiTreeUtil.getParentOfType(element, ODTScript.class) == commonParent &&
                    element.getTextOffset() < variableWrapper.getTextOffset();
        }
        return commonParent instanceof ODTDefineQueryStatement;
    }
}
