package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.inspection.quikfix.ODTRemoveUnusedLocalQuickFix;
import com.misset.opp.odt.psi.impl.variable.ODTBaseVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class ODTUnusedVariablesInspection extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect declared variables & DEFINE input parameters in the ODT language that are not used anywhere.\n";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof ODTBaseVariable)) {
                    return;
                }
                ODTBaseVariable variable = (ODTBaseVariable) element;
                if ((variable).isUnused()) {
                    holder.registerProblem(
                            variable,
                            variable.getName() + " is never used",
                            LIKE_UNUSED_SYMBOL,
                            ODTRemoveUnusedLocalQuickFix.getRemoveLocalQuickFix(variable.getName()));
                }
            }


        };
    }
}
