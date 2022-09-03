package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTOperatorCall;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTStyleInspectionUnnecessaryParentheses extends LocalInspectionTool {

    protected static final String WARNING = "Unnecessary parentheses";

    @Override
    @Nullable
    @Nls
    public String getStaticDescription() {
        return "Inspects code for unnecessary use of parentheses";
    }

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                          boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTOperatorCall) {
                    inspectOperatorCall(holder, (ODTOperatorCall) element);
                } else if (element instanceof ODTDefineStatement) {
                    inspectDefineStatement(holder, (ODTDefineStatement) element);
                }
            }
        };
    }

    private void inspectOperatorCall(@NotNull ProblemsHolder holder,
                                     @NotNull ODTOperatorCall operatorCall) {
        ODTSignature signature = operatorCall.getSignature();
        if (signature == null ||
                !signature.getSignatureArgumentList().isEmpty() ||
                operatorCall.getFlagSignature() != null) {
            return;
        }
        holder.registerProblem(signature, WARNING, ProblemHighlightType.WARNING, getReplaceQuickFix());
    }

    private void inspectDefineStatement(@NotNull ProblemsHolder holder,
                                        @NotNull ODTDefineStatement defineStatement) {
        ODTDefineParam defineParam = defineStatement.getDefineParam();
        if (defineParam != null && defineParam.getVariableList().isEmpty()) {
            holder.registerProblem(defineParam, WARNING, ProblemHighlightType.WARNING, getReplaceQuickFix());
        }
    }

    private LocalQuickFix getReplaceQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Remove parentheses";
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                Optional.of(descriptor.getPsiElement())
                        .ifPresent(PsiElement::delete);
            }
        };
    }
}
