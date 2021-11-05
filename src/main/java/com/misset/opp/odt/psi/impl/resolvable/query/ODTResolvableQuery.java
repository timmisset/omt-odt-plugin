package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableQuery extends ASTWrapperPsiElement implements ODTQuery, ODTResolvable {
    public ODTResolvableQuery(@NotNull ASTNode node) {
        super(node);
    }
}
