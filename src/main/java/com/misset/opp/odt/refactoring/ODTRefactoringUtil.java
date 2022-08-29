package com.misset.opp.odt.refactoring;

import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.odt.psi.ODTScriptLine;

public class ODTRefactoringUtil {

    private ODTRefactoringUtil() {
        // emptry constructor
    }

    public static void removeScriptline(PsiElement element) {
        ODTScriptLine scriptLine = PsiTreeUtil.getParentOfType(element, ODTScriptLine.class);
        if (scriptLine == null) {
            return;
        }
        ODTRefactoringUtil.removeWhitespace(scriptLine);
        ASTDelegatePsiElement.deleteElementFromParent(scriptLine);
    }

    public static void removeWhitespace(PsiElement element) {
        PsiElement nextLeaf = PsiTreeUtil.nextLeaf(element);
        if (PsiUtilCore.getElementType(nextLeaf) == TokenType.WHITE_SPACE) {
            nextLeaf.delete();
        }
    }

}
