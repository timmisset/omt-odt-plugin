package com.misset.opp.odt.psi.impl.calls;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.references.ODTCallReference;
import org.jetbrains.annotations.NotNull;

public abstract class ODTBaseCall extends ASTWrapperPsiElement implements PsiNamedElement {
    public ODTBaseCall(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new ODTCallReference(this, getCallName().getTextRangeInParent());
    }

    public abstract ODTCallName getCallName();

    /**
     * Distinct name for the call, when calling a command it will include the AT(@) symbol
     */
    public abstract String getCallId();

    @Override
    public String getName() {
        return getCallName().getText();
    }
}
