package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.inspection.quikfix.ODTRemoveUnusedLocalQuickFix;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class ODTUnusedDefineStatementsInspection extends LocalInspectionTool {

    public static final String DESCRIPTION = "Inspect declared DEFINE statements in the ODT language that are not used anywhere.";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return DESCRIPTION;
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof ODTDefineStatement)) {
                    return;
                }
                ODTDefineStatement defineStatement = (ODTDefineStatement) element;

                final PsiNamedElement namedElement = (PsiNamedElement) element;
                if ((defineStatement).isUnused()) {
                    holder.registerProblem(
                            defineStatement.getDefineName(),
                            namedElement.getName() + " is never used",
                            LIKE_UNUSED_SYMBOL,
                            ODTRemoveUnusedLocalQuickFix.getRemoveLocalQuickFix(namedElement.getName()));
                }
            }
        };
    }
}
