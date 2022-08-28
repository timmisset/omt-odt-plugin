package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;

public abstract class ODTBaseDefinePrefix extends ODTASTWrapperPsiElement implements ODTDefinePrefix, PsiPrefix {
    protected ODTBaseDefinePrefix(@NotNull ASTNode node) {
        super(node);
    }

    public String getNamespace() {
        final String text = getLastChild().getText();
        return text.length() > 2 ? text.substring(1, text.length() - 1) : text;
    }

    @Override
    public String getName() {
        return getNamespacePrefix().getName();
    }

    @Override
    public PsiElement getNamePsiElement() {
        return getNamespacePrefix();
    }

    @Override
    public PsiElement getNamespacePsiElement() {
        return getLastChild();
    }
}
