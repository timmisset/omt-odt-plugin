package com.misset.opp.odt.psi.impl.call;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;

public interface ODTCall extends PsiNamedElement, ODTResolvable, ODTDocumented {
    Callable getCallable();
    ODTCallName getCallName();
}
