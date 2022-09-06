package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTCommandCall;
import org.jetbrains.annotations.NotNull;

public abstract class ODTCommandCallImpl extends ODTResolvableCall implements ODTCommandCall {
    protected ODTCommandCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getCallId() {
        return "@" + getName();
    }

}
