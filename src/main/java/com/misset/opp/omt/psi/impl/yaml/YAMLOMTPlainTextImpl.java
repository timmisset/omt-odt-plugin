package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.injection.InjectionHost;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.List;

/**
 * By overriding the default YAMLPlainTextImpl we can implement PsiNamedElement and use the
 * refactor-rename, find-usage etc.
 */
@OMTOverride
public class YAMLOMTPlainTextImpl extends YAMLPlainTextImpl implements PsiNamedElement, InjectionHost {
    public YAMLOMTPlainTextImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(getContainingFile());
    }

    private transient OMTYamlDelegate delegate;

    private OMTYamlDelegate getDelegate() {
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
    public List<TextRange> getTextRanges() {
        return YAMLInjectableUtil.getTextRanges(this);
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getDelegate().delete();
    }
}
