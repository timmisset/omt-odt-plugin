package com.misset.opp.odt.psi.impl.callables;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.odt.psi.ODTDefineName;
import org.jetbrains.annotations.NotNull;

public abstract class ODTDefineStatement extends ASTWrapperPsiElement implements ODTCallable {
    public ODTDefineStatement(@NotNull ASTNode node) {
        super(node);
    }

    abstract public ODTDefineName getDefineName();

    public String getName() {
        if(this instanceof ODTDefineCommandStatement) {
            return "@" + getDefineName().getText();
        }
        return getDefineName().getText();
    }

    @Override
    public boolean isLocalCommand() {
        return false;
    }

    @Override
    public boolean isBuiltinCommand() {
        return false;
    }

    @Override
    public boolean hasPsiElement() {
        return true;
    }
}
