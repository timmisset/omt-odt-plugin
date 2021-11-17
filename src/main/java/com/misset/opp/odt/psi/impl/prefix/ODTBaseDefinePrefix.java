package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

public abstract class ODTBaseDefinePrefix extends ODTASTWrapperPsiElement implements ODTDefinePrefix {
    public ODTBaseDefinePrefix(@NotNull ASTNode node) {
        super(node);
    }

    public String getNamespace() {
        final String text = getLastChild().getText();
        return text.length() > 2 ? text.substring(1, text.length() - 1) : text;
    }

    public String getPrefix() {
        return getNamespacePrefix().getName();
    }
}
