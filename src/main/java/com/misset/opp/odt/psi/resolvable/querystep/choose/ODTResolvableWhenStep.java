package com.misset.opp.odt.psi.resolvable.querystep.choose;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQueryStep;

public interface ODTResolvableWhenStep extends ODTResolvableQueryStep {

    ODTQuery getCondition();

    ODTQuery getThen();

}
