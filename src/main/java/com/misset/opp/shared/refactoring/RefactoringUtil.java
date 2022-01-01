package com.misset.opp.shared.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.resolvable.psi.PsiCall;

public class RefactoringUtil {

    public static void removeParameterFromCall(PsiReference reference, int index) {
        PsiElement element = reference.getElement();
        if (element instanceof PsiCall) {
            PsiCall call = (PsiCall) element;
            call.removeArgument(index);
        }
    }

}
