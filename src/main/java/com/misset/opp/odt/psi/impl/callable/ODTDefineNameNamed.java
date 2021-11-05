package com.misset.opp.odt.psi.impl.callable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.omt.OMTFileType;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Named version of th ODTDefineName to allow FindUsage and renaming
 */
public abstract class ODTDefineNameNamed extends ASTWrapperPsiElement implements PsiNamedElement, ODTResolvable {

    private static final Key<CachedValue<SearchScope>> USAGE_SEARCH_SCOPE = new Key<>("USAGE_SEARCH_SCOPE");

    public ODTDefineNameNamed(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public @Nullable @NlsSafe String getName() {
        return getText();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return CachedValuesManager.getCachedValue(this, USAGE_SEARCH_SCOPE, () ->
                new CachedValueProvider.Result<>(GlobalSearchScope.FilesScope.getScopeRestrictedByFileTypes(
                        GlobalSearchScope.projectScope(getProject()),
                        OMTFileType.INSTANCE
                ), ModificationTracker.NEVER_CHANGED));
    }

    @Override
    public Set<OntResource> resolve() {
        return ((ODTDefineStatement)getParent()).resolve();
    }
}
