package com.misset.opp.omt.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTPrefixMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;

import java.util.Collection;
import java.util.Optional;

/**
 * Refactor-rename of a prefix should only be searched within the current file
 * Since this is a native Yaml element, we need to provide the RenameProcessor to enforce the scope
 */
public class OMTPrefixRenameProcessor extends RenamePsiElementProcessor {
    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return getMetaType(element) != null;
    }

    private OMTPrefixMetaType getMetaType(PsiElement element) {
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(element.getProject())
                        .getMetaTypeProxy(element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTPrefixMetaType.class::isInstance)
                .map(OMTPrefixMetaType.class::cast)
                .orElse(null);
    }

    @Override
    public @NotNull Collection<PsiReference> findReferences(@NotNull PsiElement element,
                                                            @NotNull SearchScope searchScope,
                                                            boolean searchInCommentsAndStrings) {
        return super.findReferences(element,
                new LocalSearchScope(element.getContainingFile()),
                searchInCommentsAndStrings);
    }
}
