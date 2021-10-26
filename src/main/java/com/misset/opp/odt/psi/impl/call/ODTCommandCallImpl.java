package com.misset.opp.odt.psi.impl.call;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTCommandCall;
import org.jetbrains.annotations.NotNull;

/**
 * The OMTCommandCallImpl wraps the auto-generated version
 */
public abstract class ODTCommandCallImpl extends ODTBaseCall implements ODTCommandCall {
    public ODTCommandCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public String getCallId() {
        return "@" + getName();
    }

}
