package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * By overriding the default YAMLPlainTextImpl we can implement PsiNamedElement and use the
 * refactor-rename, find-usage etc.
 */
public class YAMLOMTPlainTextImpl extends YAMLPlainTextImpl implements PsiNamedElement, PsiLanguageInjectionHost, YAMLScalar {
    public YAMLOMTPlainTextImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(getContainingFile());
    }

    private transient PsiNamedElement delegate;

    private PsiNamedElement getDelegate() {
        if (delegate == null) {
            delegate = OMTYamlDelegateFactory.createDelegate(this);
        }
        return delegate;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return getDelegate().getReferences();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return getDelegate().setName(name);
    }

    @Override
    public String getName() {
        return getDelegate().getName();
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getDelegate().delete();
    }
}
