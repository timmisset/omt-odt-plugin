package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Code inspection for all unused declarations
 */
public class ODTStyleInspectionUnnecessaryIdentifierOperator extends LocalInspectionTool {

    protected static final String UNNECESSARY_IDENTIFIER_OPERATOR = "Unnecessary Identifier operator (.)";
    protected static final String REMOVE_OPERATOR = "Remove operator";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspects coding style for unnecessary Identifier operator";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTQueryStep) {
                    inspectBaseOperator(holder, (ODTQueryStep) element);
                }
            }
        };
    }

    private void inspectBaseOperator(@NotNull ProblemsHolder holder,
                                     @NotNull ODTQueryStep queryStep) {
        if (queryStep instanceof ODTIdentifierStep && !onlyStepInPath(queryStep)) {
            holder.registerProblem(queryStep,
                    UNNECESSARY_IDENTIFIER_OPERATOR, ProblemHighlightType.WARNING, getRemoveQueryStepQuickFix());
        }
    }

    private LocalQuickFix getRemoveQueryStepQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return REMOVE_OPERATOR;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                /*
                    For some reason, removing the step and adjacent forward_slash causes the InjectedLanguage to get confused
                    Instead, filter out the current step and generate a new query, then replace it and catch the error
                 */
                final PsiElement psiElement = descriptor.getPsiElement();
                final ODTQueryOperationStep operationStep = PsiTreeUtil.getParentOfType(psiElement,
                        ODTQueryOperationStep.class);
                if (operationStep == null) {
                    return;
                }

                final ODTQueryPath queryPath = operationStep.getParent();
                String filteredQuery = queryPath.getQueryOperationStepList()
                        .stream()
                        .map(step -> getStep(step, operationStep))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(" / "));
                if (queryPath.getText().trim().startsWith("/")) {
                    filteredQuery = "/" + filteredQuery;
                }
                // workaround for: https://youtrack.jetbrains.com/issue/IDEA-282939
                // cannot remove psi element from injected language??
                // first replace with a completely different query, then replace that with the correct one
                final PsiElement replace = queryPath.replace(ODTElementGenerator.getInstance(project)
                        .fromFile("'I really hope that no-one every has this as an actual value for a query'",
                                ODTQueryPath.class));
                replace.replace(ODTElementGenerator.getInstance(project)
                        .fromFile(filteredQuery, ODTQueryPath.class));
            }
        };
    }

    private boolean onlyStepInPath(@NotNull ODTQueryStep queryStep) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(queryStep, ODTQueryPath.class))
                .map(ODTQueryPath::getQueryOperationStepList)
                .map(steps -> steps.size() == 1)
                .orElse(false);
    }

    private String getStep(ODTQueryOperationStep operationStep, ODTQueryOperationStep identifierStep) {
        if (operationStep == identifierStep) {
            ODTQueryFilter filter = PsiTreeUtil.getChildOfType(operationStep, ODTQueryFilter.class);
            if (filter != null) {
                return filter.getText();
            } else {
                return null;
            }
        } else {
            return operationStep.getText();
        }
    }

}
