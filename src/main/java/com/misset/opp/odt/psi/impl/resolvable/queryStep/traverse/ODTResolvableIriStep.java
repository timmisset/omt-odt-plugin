package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.misset.opp.odt.psi.ODTIriStep;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableIriStep extends ODTResolvableQueryForwardStep implements ODTIriStep {
    public ODTResolvableIriStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String calculateFullyQualifiedUri() {
        final String text = getText();
        if (text.startsWith("<json:")) {
            // we cannot evaluate a json-schema
            return null;
        }
        return text.substring(1, text.length() - 1);
    }

    @Override
    protected TextRange getModelReferenceTextRange() {
        return TextRange.create(1, getTextLength() - 1);
    }
}
