package com.misset.opp.odt.psi.wrapping.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.wrapping.ODTVariableWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTDefinedVariableImpl extends ODTBaseVariable implements ODTVariableWrapper {
    public ODTDefinedVariableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isDefinedVariable() {
        return true;
    }

    public boolean canBeDefinedVariable(ODTVariableWrapper variableWrapper) {
        return sameNameAs(variableWrapper) && isAccessibleTo(variableWrapper);
    }

    private boolean sameNameAs(ODTVariableWrapper variableWrapper) {
        return Optional.ofNullable(variableWrapper)
                .map(PsiNamedElement::getName)
                .map(s -> s.equals(getName()))
                .orElse(false);
    }

    private boolean isAccessibleTo(ODTVariableWrapper variableWrapper) {
        if(variableWrapper == null || !variableWrapper.isValid() || !isValid()) { return false; }
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(this, variableWrapper);
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
            return PsiTreeUtil.getParentOfType(this, ODTScript.class) == commonParent &&
                    this.getTextOffset() < variableWrapper.getTextOffset();
        }
        return commonParent instanceof ODTDefineQueryStatement;
    }
}
