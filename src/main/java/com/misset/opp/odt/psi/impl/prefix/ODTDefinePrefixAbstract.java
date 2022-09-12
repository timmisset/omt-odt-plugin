package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;

public abstract class ODTDefinePrefixAbstract extends ODTASTWrapperPsiElement implements ODTDefinePrefix, PsiPrefix, PsiNamedElement {
    protected ODTDefinePrefixAbstract(@NotNull ASTNode node) {
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

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        ODTScriptLine scriptLine = ODTElementGenerator.getInstance(getProject()).createDefinePrefix(name, getNamespace());
        ODTScriptLine parentOfType = PsiTreeUtil.getParentOfType(this, ODTScriptLine.class);
        if (parentOfType == null) {
            return this;
        }
        PsiElement replaced = parentOfType.replace(scriptLine);
        return PsiTreeUtil.findChildOfType(replaced, ODTDefinePrefix.class);
    }
}
