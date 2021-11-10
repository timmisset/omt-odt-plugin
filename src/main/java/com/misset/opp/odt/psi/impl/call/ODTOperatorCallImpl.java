package com.misset.opp.odt.psi.impl.call;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTOperatorCall;
import org.jetbrains.annotations.NotNull;

public abstract class ODTOperatorCallImpl extends ODTBaseCall implements ODTOperatorCall {
    public ODTOperatorCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getCallId() {
        return getName();
    }

}
