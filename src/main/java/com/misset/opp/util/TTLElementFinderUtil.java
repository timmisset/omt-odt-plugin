package com.misset.opp.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLObjectStubIndex;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class TTLElementFinderUtil {
    private TTLElementFinderUtil() {
        // empty constructor
    }

    /**
     * Returns the IRI element of a Subject
     */
    public static ResolveResult[] getSubjectResolveResult(Project project, String fullyQualifiedUri) {
        if (fullyQualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return StubIndex.getElements(
                        TTLSubjectStubIndex.KEY,
                        fullyQualifiedUri,
                        project,
                        getScope(project),
                        TTLSubject.class
                ).stream()
                .map(TTLSubject::getIri)
                .filter(Objects::nonNull)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @NotNull
    public static ResolveResult[] getObjectResolveResult(Project project,
                                                         String fullyQualifiedUri,
                                                         Predicate<TTLObject> filter) {

        return StubIndex.getElements(
                        TTLObjectStubIndex.KEY,
                        fullyQualifiedUri,
                        project,
                        getScope(project),
                        TTLObject.class
                ).stream()
                .filter(TTLObject::isPredicate)
                .filter(filter)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    private static GlobalSearchScope getScope(Project project) {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), TTLFileType.INSTANCE);
    }

}
