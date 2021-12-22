package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.OMTYamlReferenceContributor;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

/**
 * The OMT key:value pairs of interest are the ones that are used for prefix declarations,
 * the imports (path: members as sequence value) and the ModelItems.
 * <p>
 * Since YAMLKeyValueImpl is a ContributedReferenceHost, its getReferences() isn't called, but rather
 * it expects to always get the references via the ReferencesProvidersRegistry
 * <p>
 * The setName and getName implementation of the YamlKeyValue can be used, this should not be deferred
 * to the delegate.
 *
 * @see OMTYamlReferenceContributor
 */
@OMTOverride
public class YAMLOMTKeyValueImpl extends YAMLKeyValueImpl {
    public YAMLOMTKeyValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    private OMTYamlDelegate delegate;

    private OMTYamlDelegate getDelegate() {
        if (delegate == null) {
            delegate = OMTYamlDelegateFactory.createDelegate(this);
        }
        return delegate;
    }

    @Override
    public PsiReference getReference() {
        return getDelegate().getReference();
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return getDelegate().getReferences();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return getDelegate().getUseScope();
    }
}
