// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ODTOperatorCall extends ODTQueryStep, ODTCall {

  @NotNull
  ODTCallName getCallName();

  @Nullable
  ODTFlagSignature getFlagSignature();

  @Nullable
  ODTSignature getSignature();

}
