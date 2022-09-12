// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.misset.opp.odt.psi.resolvable.callable.ODTResolvableDefineQueryStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ODTDefineQueryStatement extends ODTStatement, ODTResolvableDefineQueryStatement {

    @NotNull
    ODTDefineName getDefineName();

    @Nullable
    ODTDefineParam getDefineParam();

    @NotNull
    ODTQuery getQuery();

}
