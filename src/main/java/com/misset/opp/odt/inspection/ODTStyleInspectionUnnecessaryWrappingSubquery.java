package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.odt.psi.ODTNegatedStep;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryArray;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTSubQuery;
import com.misset.opp.odt.psi.impl.ODTSubQueryImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTStyleInspectionUnnecessaryWrappingSubquery extends LocalInspectionTool {

    protected static final String WARNING = "Unnecessary wrapping of Subquery";

    @Override
    @Nullable
    @Nls
    public String getStaticDescription() {
        return "Inspects coding style for unnecessary wrapping of queries steps. For example: <br>" +
                "($variableA == 'a') AND ($variableB == 'b') can be simply $variableA == 'a' AND $variableB == 'b'<br>" +
                "<br>" +
                "This also holds up for IF / ELSE IF boolean statements.";
    }

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                          boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTSubQueryImpl) {
                    inspectSubQuery(holder, (ODTSubQueryImpl) element);
                }
            }
        };
    }

    private void inspectSubQuery(@NotNull ProblemsHolder holder,
                                 @NotNull ODTSubQuery subQuery) {
        if (shouldBeWrapped(subQuery)) {
            return;
        }
        holder.registerProblem(subQuery, WARNING, ProblemHighlightType.WARNING, getReplaceQuickFix());
    }

    private LocalQuickFix getReplaceQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Unwrap";
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                Optional.of(descriptor.getPsiElement())
                        .filter(ODTSubQuery.class::isInstance)
                        .map(ODTSubQuery.class::cast)
                        .ifPresent(odtSubQuery -> odtSubQuery.replace(odtSubQuery.getQuery()));
            }
        };
    }

    public boolean shouldBeWrapped(ODTSubQuery subQuery) {
        if (isDecorated(subQuery)) {
            return true;
        }
        final ODTQuery query = subQuery.getQuery();
        // (true AND false) OR (false AND true) <-- ODTBooleanStatements should be wrapped
        // (1 | 2) / ont:someThing <-- ODTQueryArrays should be wrapped
        // NOT (some / query / path / traversion) <-- should also be wrapped
        return query instanceof ODTBooleanStatement ||
                query instanceof ODTQueryArray ||
                subQuery.getParent() instanceof ODTNegatedStep;
    }

    private boolean isDecorated(ODTSubQuery subQuery) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(subQuery, ODTQueryOperationStep.class))
                .map(ODTQueryOperationStep::getStepDecorator)
                .isPresent();
    }

}
