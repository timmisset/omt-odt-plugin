package com.misset.opp.odt.psi.impl.callable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import org.jetbrains.annotations.NotNull;

public abstract class ODTDefineStatement extends ASTWrapperPsiElement implements Callable {
    public ODTDefineStatement(@NotNull ASTNode node) {
        super(node);
    }

    abstract public ODTDefineName getDefineName();

    @Override
    public String getDescription(String context) {
        return null;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 0;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    public String getName() {
        return getDefineName().getText();
    }

    public abstract ODTDefineParam getDefineParam();
}
