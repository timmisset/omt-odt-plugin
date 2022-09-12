package com.misset.opp.odt.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides a filter that determines if the suggested set with resources should be shown as completion.
 * For example:
 * VAR $variable = 'test';
 *
 * @LOG(<caret>); <-- should show completion for $variable since a string is acceptable for @LOG first argument
 * @CLEAR_GRAPH(<caret>); <-- should not show completion for $variable since it requires a NamedGraph type
 * <p>
 * The CompletionContributor should only check against the first ODTTypeFilter parent they encounter in the PsiTree
 * The ODTTypeFilter must always return a filter
 * The ODTTypeFilter determines if the position of the provided element in the ODTTypeFilter makes filtering applicable
 * usually this is based on the element being the first-deepest child of the ODTTypeFilter but it can also be true when
 * it's the right-side of a comparison
 */
public interface ODTTypeFilterProvider extends PsiElement {
    Predicate<Set<OntResource>> ACCEPT_ALL = resources -> true;

    static Predicate<Set<OntResource>> getFirstTypeFilter(PsiElement element) {
        PsiElement completionPlaceholderTopElement = getCompletionPlaceholderTopElement(element);
        ODTTypeFilterProvider typeFilterProvider = PsiTreeUtil.getParentOfType(completionPlaceholderTopElement, ODTTypeFilterProvider.class);
        if (typeFilterProvider == null) {
            return ACCEPT_ALL;
        } else {
            return typeFilterProvider.getTypeFilter(completionPlaceholderTopElement);
        }
    }

    Predicate<Set<OntResource>> getTypeFilter(PsiElement element);

    static PsiElement getCompletionPlaceholderTopElement(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, ODTCall.class);
    }
}
