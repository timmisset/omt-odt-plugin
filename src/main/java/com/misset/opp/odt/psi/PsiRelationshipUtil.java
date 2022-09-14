package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;

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

}
