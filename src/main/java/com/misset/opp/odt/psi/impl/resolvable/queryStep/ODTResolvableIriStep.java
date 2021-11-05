package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTIriStep;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableIriStep extends ODTResolvableQueryForwardStep implements ODTIriStep {
    public ODTResolvableIriStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getFullyQualifiedUri() {
        final String text = getText();
        return text.substring(1, text.length()-1);
    }
}
