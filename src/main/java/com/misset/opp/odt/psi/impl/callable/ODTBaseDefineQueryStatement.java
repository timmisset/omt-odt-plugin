package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTDefineName;
import org.jetbrains.annotations.NotNull;

public abstract class ODTBaseDefineQueryStatement extends ODTDefineStatement {
    public ODTBaseDefineQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    abstract public ODTDefineName getDefineName();

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    public String getCallId() { return getName(); }
}
