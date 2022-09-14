package com.misset.opp.odt.psi.resolvable.querystep;

import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;

public interface ODTResolvableQueryStep extends ODTResolvable {
    /**
     * Returns the resolve QueryOperation container of this step
     * If steps are further encapsulated, this method should be overridden to return the QueryOperation
     */
    ODTQueryOperationStep getResolvableParent();

    /**
     * Returns true if this step is the first step in the containing QueryPath
     */
    boolean isRootStep();

    /**
     * Returns the range within this step that should be targeted by the annotation/inspection tools
     */
    PsiElement getAnnotationRange();
}
