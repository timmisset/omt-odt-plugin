package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTSignature;
import org.jetbrains.annotations.NotNull;

public abstract class ODTCommandCallImpl extends ODTResolvableCall implements ODTCommandCall {
    public ODTCommandCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getCallId() {
        return "@" + getName();
    }

    @Override
    protected void removeAllSignatureArguments() {
        ODTSignature signature = getSignature();
        if (signature != null) {
            ODTCall call = ODTElementGenerator.getInstance(getProject()).createNoArgsCall("Call", true);
            if (call != null && call.getSignature() != null) {
                signature.replace(call.getSignature());
            }
        }
    }
}
