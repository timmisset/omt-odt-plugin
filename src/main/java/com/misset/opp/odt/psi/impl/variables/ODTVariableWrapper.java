package com.misset.opp.odt.psi.impl.variables;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variables.delegates.ODTVariableDelegate;

/**
 * Wrapper interface that combines the PsiNamedElement for handeling the Node and the Delegate since all
 * VariableDelegate interface methods should be available to the ODTVariable.
 */
public interface ODTVariableWrapper extends PsiNamedElement, ODTVariableDelegate {

    boolean sameNameAs(ODTVariable variable);

}
