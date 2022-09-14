package com.misset.opp.odt.psi.resolvable;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import org.jetbrains.annotations.NotNull;

public interface ODTResolvableCollectionStatement {

    @NotNull
    ODTQuery getQuery();

    @NotNull
    ODTResolvableValue getResolvableValue();

}
