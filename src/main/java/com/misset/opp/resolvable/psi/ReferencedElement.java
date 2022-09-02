package com.misset.opp.resolvable.psi;

import com.intellij.psi.PsiElement;

/**
 * Element that is used to describe a reference to another element.
 * For example, an import statement. The usage of the import references the import member but the import member
 * itself is not the final element. Another usage is !Ref elements in OMT (currently not supported yet).
 */
public interface ReferencedElement {

    PsiElement getFinalElement();

}
