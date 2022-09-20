package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TTLClassReference extends PsiReferenceBase.Poly<TTLQualifiedIriResolver> implements PsiPolyVariantReference {
    public TTLClassReference(TTLQualifiedIriResolver localname, TextRange textRange) {
        super(localname, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String qualifiedUri = getElement().getQualifiedIri();
        if (qualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }

        ProjectFileIndex projectFileIndex = ProjectFileIndex.getInstance(myElement.getProject());
        Module module = projectFileIndex.getModuleForFile(myElement.getContainingFile().getVirtualFile());

        GlobalSearchScope searchScope = module != null ?
                GlobalSearchScope.moduleWithDependentsScope(module) :
                GlobalSearchScope.everythingScope(myElement.getProject());
        searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes(searchScope, TTLFileType.INSTANCE);
        return StubIndex.getElements(TTLSubjectStubIndex.KEY,
                        qualifiedUri,
                        getElement().getProject(),
                        searchScope,
                        TTLSubject.class)
                .stream()
                .map(TTLSubject::getIri)
                .filter(Objects::nonNull)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        String qualifiedIri = myElement.getQualifiedIri();
        return qualifiedIri != null &&
                element instanceof TTLQualifiedIriResolver &&
                qualifiedIri.equals(((TTLQualifiedIriResolver) element).getQualifiedIri());
    }
}
