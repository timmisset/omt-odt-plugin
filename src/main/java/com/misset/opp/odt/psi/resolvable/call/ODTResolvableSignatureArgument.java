package com.misset.opp.odt.psi.resolvable.call;

import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;

public interface ODTResolvableSignatureArgument extends ODTResolvable, ODTTypeFilterProvider {

    boolean isPrimitiveArgument();

}
