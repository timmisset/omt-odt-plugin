package com.misset.opp.odt.psi.impl.variables;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScript;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTDefinedVariableImpl extends ODTBaseVariable {
    public ODTDefinedVariableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isDefinedVariable() {
        return true;
    }

    public boolean canBeDefinedVariable(ODTBaseVariable variableWrapper) {
        return sameNameAs(variableWrapper) && isAccessibleTo(variableWrapper);
    }

    private boolean sameNameAs(ODTBaseVariable variableWrapper) {
        return Optional.ofNullable(variableWrapper)
                .map(PsiNamedElement::getName)
                .map(s -> s.equals(getName()))
                .orElse(false);
    }

    private boolean isAccessibleTo(ODTBaseVariable variableWrapper) {
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
