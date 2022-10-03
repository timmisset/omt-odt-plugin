package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;

import java.util.*;
import java.util.stream.Collectors;

public class PsiRelationshipUtil {

    private PsiRelationshipUtil() {
        // empty constructor
    }

    /**
     * Determines if there can be a declaration/usage relationship between 2 elements based solely on
     * their position in the PsiTree. Taking script block structures and declaration before usage
     * into consideration.
     */
    public static boolean canBeRelatedElement(PsiElement declaringElement, PsiElement usageElement) {
        if (usageElement.getTextOffset() < declaringElement.getTextOffset()) {
            return false;
        }

        // The comments use a variable relationship between declaration and usage as example
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(declaringElement, usageElement);
        // DEFINE COMMAND command($variable) => { $variable } <-- same common parent as ODTDefineStatement OK
        // DEFINE COMMAND command => { VAR $variable; @LOG($variable); } <-- common parent will be script, so not OK (yet)
        if (commonParent instanceof ODTDefineStatement) {
            return true;
        }

        if (commonParent instanceof ODTScript) {
            // only when the declared variable is in the root of the common parent:
            // VAR $variable
            // IF true { @LOG($variable); } <-- passed

            // IF true { VAR $variable; } ELSE { @LOG($variable); } <-- failed
            return PsiTreeUtil.getParentOfType(declaringElement, ODTDefineStatement.class, ODTScript.class) == commonParent;
        }
        // or, if the declaring element is in the root of the file (relative position is already checked in the first assertion)
        return commonParent instanceof ODTFile;
    }

    /**
     * Returns a list with all elements that are related to the target by resolving to the same
     * declaring element and by passing the canBeRelatedElement test. In the test the discovered element
     * must be in a position to be the declaring element of the target.
     * The results are ordered by proximity to the target, the closest first.
     * Only related elements in the same file are considered
     * <p>
     * VAR $declared = 'test'; <-- part of the result (index 1)
     * $declared = 'new Value; <-- part of the result (index 0)
     * IF ... {
     * $declared = true; // <-- not part of the result
     * } ELSE {
     *
     * @LOG($declared); // <-- target
     * $declared = false; // <-- not part of the result
     * }
     */
    public static List<PsiElement> getRelatedElements(PsiElement target) {
        PsiElement declaringElement = Optional.ofNullable(target.getReference()).map(PsiReference::resolve).orElse(null);
        if (declaringElement == null) {
            return Collections.emptyList();
        }

        List<PsiElement> relatedItems = ReferencesSearch.search(declaringElement, GlobalSearchScope.fileScope(target.getContainingFile()))
                .findAll()
                .stream()
                .map(PsiReference::getElement)
                .filter(relatedElement -> canBeRelatedElement(relatedElement, target))
                .sorted(Comparator.comparing(PsiElement::getTextOffset).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
        relatedItems.add(declaringElement);
        return relatedItems;
    }

}
