package com.misset.opp.odt.psi.variable;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.resolvable.psi.PsiVariable;

/**
 * Wrapper interface that combines the PsiNamedElement for handling the Node and the Delegate since all
 * VariableDelegate interface methods should be available to the ODTVariable.
 * <p>
 * This interface should not be used directly, instead use the ODTVariable interface
 */
public interface ODTWrapperVariable extends PsiNamedElement, ODTVariableDelegate, PsiVariable {

    boolean sameNameAs(ODTVariable variable);

    ODTVariableDelegate getDelegate();

    boolean canBeAnnotated();
}
