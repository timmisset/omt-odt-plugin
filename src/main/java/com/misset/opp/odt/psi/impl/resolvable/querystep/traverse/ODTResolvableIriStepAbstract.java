package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTIriStep;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableIriStepAbstract extends ODTResolvableQueryForwardStepAbstract implements ODTIriStep {
    protected ODTResolvableIriStepAbstract(@NotNull ASTNode node) {
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
    public TextRange getModelReferenceTextRange() {
        return TextRange.create(1, getTextLength() - 1);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        String iri = getNamespace() + name;
        ODTIriStep iriStep = ODTElementGenerator.getInstance(getProject()).fromFile(String.format("<%s>", iri), ODTIriStep.class);
        return replace(iriStep);
    }
}
