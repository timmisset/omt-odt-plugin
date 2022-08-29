package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.operators.BuiltInBooleanOperator;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableEquationStatement;
import com.misset.opp.resolvable.psi.PsiCall;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTStyleInspectionUnnecessaryTrueCondition extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspects coding style for unnecessary True condition, i.e. IF $myBooleanVariable == true { } => IF $myBooleanVariable {}";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTResolvableEquationStatement) {
                    inspectBaseOperator(holder, (ODTResolvableEquationStatement) element);
                }
            }
        };
    }

    private void inspectBaseOperator(@NotNull ProblemsHolder holder,
                                     @NotNull ODTResolvableEquationStatement equationStatement) {

        boolean insideBooleanOperator = Optional.ofNullable(PsiTreeUtil.getParentOfType(equationStatement, ODTCall.class))
                .map(PsiCall::getCallable)
                .map(BuiltInBooleanOperator.class::isInstance)
                .orElse(false);
        if (insideBooleanOperator) {
            return;
        }

        List<ODTQuery> queryList = equationStatement.getQueryList();
        if (queryList.size() != 2) {
            return;
        }

        ODTQuery leftHand = queryList.get(0);
        ODTQuery rightHand = queryList.get(1);

        if (leftHand.getText().equals("true")) {
            addWarning(holder, equationStatement, rightHand);
        } else if (rightHand.getText().equals("true")) {
            addWarning(holder, equationStatement, leftHand);
        }
    }

    private void addWarning(@NotNull ProblemsHolder holder,
                            @NotNull ODTResolvableEquationStatement equationStatement,
                            ODTQuery necessaryPart) {
        holder.registerProblem(equationStatement,
                equationStatement.getText() + " can be simplified to " + necessaryPart.getText(),
                getReplaceQuickFix(necessaryPart));
    }

    private LocalQuickFix getReplaceQuickFix(ODTQuery necessaryPart) {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Replace";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return "Replace with: " + ReadAction.compute(necessaryPart::getText);
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                descriptor.getPsiElement().replace(necessaryPart);
            }
        };
    }
}
