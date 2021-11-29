package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTVariableNameReference extends OMTPlainTextReference {
    private final TextRange realTextRange;

    public OMTVariableNameReference(@NotNull YAMLPlainTextImpl element,
                                    TextRange textRange) {
        super(element, TextRange.EMPTY_RANGE);
        this.realTextRange = textRange;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        if (!newElementName.startsWith("$")) {
            newElementName = "$" + newElementName;
        }
        return handleElementRename(newElementName, realTextRange);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return PsiElementResolveResult.createResults(myElement);
    }
}
