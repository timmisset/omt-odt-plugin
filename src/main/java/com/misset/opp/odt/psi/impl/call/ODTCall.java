package com.misset.opp.odt.psi.impl.call;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.annotation.ODTAnnotatedElement;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;

public interface ODTCall extends PsiNamedElement, ODTResolvable, ODTAnnotatedElement {
    Callable getCallable();
    ODTCallName getCallName();
}
