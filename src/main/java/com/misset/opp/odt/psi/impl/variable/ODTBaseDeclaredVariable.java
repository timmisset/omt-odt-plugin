package com.misset.opp.odt.psi.impl.variable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.impl.ODTStatementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for refactoring
 */
public abstract class ODTBaseDeclaredVariable extends ODTStatementImpl implements ODTDeclareVariable, PsiNameIdentifierOwner {
    public ODTBaseDeclaredVariable(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable ODTBaseVariable getNameIdentifier() {
        return PsiTreeUtil.findChildOfType(this, ODTBaseVariable.class);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        ODTBaseVariable identifier = getNameIdentifier();
        if (identifier == null) {
            return this;
        }
        return identifier.setName(name);
    }

    @Override
    public String getName() {
        ODTBaseVariable identifier = getNameIdentifier();
        if (identifier == null) {
            return null;
        }
        return identifier.getName();
    }
}
