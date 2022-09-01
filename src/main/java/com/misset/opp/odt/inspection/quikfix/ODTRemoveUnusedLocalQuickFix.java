package com.misset.opp.odt.inspection.quikfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;

public class ODTRemoveUnusedLocalQuickFix {
    private ODTRemoveUnusedLocalQuickFix() {
        // emptry constructor
    }

    public static LocalQuickFix getRemoveLocalQuickFix(String name) {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Remove";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return "Remove " + name;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                PsiElement psiElement = descriptor.getPsiElement();
                while (psiElement != null && !(psiElement instanceof SupportsSafeDelete)) {
                    psiElement = psiElement.getParent();
                }
                if (psiElement != null) {
                    psiElement.delete();
                }
            }
        };
    }
}
