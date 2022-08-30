package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.OMTRefactoringSupport;
import org.jetbrains.annotations.NotNull;

public class OMTRemoveQuickFix {
    private OMTRemoveQuickFix() {
        // empty constructor
    }

    public static LocalQuickFix getRemoveLocalQuickFix(String type) {
        OMTRefactoringSupport omtRefactoringSupport = new OMTRefactoringSupport();
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Remove";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return "Remove " + type;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                PsiElement psiElement = descriptor.getPsiElement();
                while (psiElement != null && !omtRefactoringSupport.isSafeDeleteAvailable(psiElement)) {
                    psiElement = psiElement.getParent();
                }
                if (psiElement != null) {
                    psiElement.delete();
                }
            }
        };
    }
}
