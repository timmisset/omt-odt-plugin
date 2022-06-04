package com.misset.opp.odt.inspection.calls.operators;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.builtin.operators.IIfOperator;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Resolvable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTOperatorInspectionIIf extends LocalInspectionTool {
    protected static final String UNNECESSARY_IIF = "Unnecessary IIF";
    protected static final String SIMPLIFY = "Simplify";
    protected static final String COMBINE = "Combine";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates specific ODT operator: IIf";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    inspectIIfOperator(holder, (ODTCall) element);
                }
            }
        };
    }

    private void inspectIIfOperator(@NotNull ProblemsHolder holder,
                                    @NotNull ODTCall call) {
        if (call.getCallable() == IIfOperator.INSTANCE) {
            if (canBeSimplified(call)) {
                holder.registerProblem(call, UNNECESSARY_IIF, ProblemHighlightType.WARNING, getSimplifyQuickfix(call));
            } else if (canBeCombined(call)) {
                holder.registerProblem(call, UNNECESSARY_IIF, ProblemHighlightType.WARNING, getCombineQuickfix(call));
            }
        }
    }

    private boolean canBeSimplified(@NotNull ODTCall call) {
        return call.getSignatureArguments()
                .stream()
                .skip(1)
                .allMatch(argument -> argument.isPrimitiveArgument() && argument.isBoolean());
    }

    private boolean canBeCombined(@NotNull ODTCall call) {
        return call.getNumberOfArguments() == 2 && Optional.ofNullable(call.getSignatureArgument(1))
                .map(Resolvable::isBoolean)
                .orElse(false);
    }

    @NotNull
    private LocalQuickFix getSimplifyQuickfix(@NotNull ODTCall call) {
        boolean negate = "false".equals(call.getSignatureValue(1));
        String query = String.format("%s%s", negate ? "NOT " : "", getCondition(call));

        return getQuickFix(SIMPLIFY, query);
    }

    @NotNull
    private LocalQuickFix getCombineQuickfix(@NotNull ODTCall call) {
        String query = String.format("%s AND %s",
                call.getSignatureValue(0),
                call.getSignatureValue(1));
        return getQuickFix(COMBINE, query);
    }

    private LocalQuickFix getQuickFix(String familyName,
                                      String query) {

        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return familyName;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                final ODTQuery replacement = ODTElementGenerator.getInstance(project).fromFile(query, ODTQuery.class);
                if (replacement == null) {
                    return;
                }
                descriptor.getPsiElement().replace(replacement);
            }
        };
    }

    private String getCondition(@NotNull ODTCall call) {
        return call.getSignatureValue(0);
    }

}
