package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.OMTYamlReferenceContributor;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Collection;

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

    private transient OMTYamlDelegate delegate;

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

    @Override
    public void delete() throws IncorrectOperationException {
        getDelegate().delete();
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
        // This is a fix for issue https://github.com/timmisset/omt-odt-plugin/issues/44
        Collection<PsiReference> referenceCollection =
                ReferencesSearch.search(this, getUseScope()).findAll();
        PsiElement rename = YAMLUtil.rename(this, newName);
        referenceCollection.forEach(reference -> reference.handleElementRename(newName));
        return rename;
    }
}
