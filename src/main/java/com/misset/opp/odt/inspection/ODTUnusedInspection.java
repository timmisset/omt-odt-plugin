package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class ODTUnusedInspection extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect declared elements in the ODT language that are not used anywhere.\n" +
                "For example, DEFINE statements, DEFINE parameters, VAR variable declarations";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof SupportsSafeDelete)) {
                    return;
                }
                SupportsSafeDelete safeDeleteElement = (SupportsSafeDelete) element;

                final PsiNamedElement namedElement = (PsiNamedElement) element;
                if ((safeDeleteElement).isUnused()) {
                    holder.registerProblem(
                            safeDeleteElement.getHighlightingTarget(),
                            namedElement.getName() + " is never used",
                            LIKE_UNUSED_SYMBOL,
                            getRemoveLocalQuickFix(namedElement.getName()));
                }
            }

            private LocalQuickFix getRemoveLocalQuickFix(String name) {
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
        };
    }
}
