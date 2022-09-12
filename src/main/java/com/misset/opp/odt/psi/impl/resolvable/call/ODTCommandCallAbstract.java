package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTCommandCall;
import org.jetbrains.annotations.NotNull;

public abstract class ODTCommandCallAbstract extends ODTResolvableCallAbstract implements ODTCommandCall {
    protected ODTCommandCallAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getCallId() {
        return "@" + getName();
    }

}
