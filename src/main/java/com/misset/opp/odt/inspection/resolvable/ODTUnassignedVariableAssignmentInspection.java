package com.misset.opp.odt.inspection.resolvable;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTAssignmentStatement;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTUsageVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.resolvable.Variable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.intellij.codeInspection.ProblemHighlightType.WARNING;

public class ODTUnassignedVariableAssignmentInspection extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect unassigned variable usage.\n" +
                "When a variable has been declared but never assigned before used.";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof ODTVariable)) {
                    return;
                }
                ODTVariable variable = (ODTVariable) element;
                ODTVariableDelegate variableDelegate = variable.getDelegate();
                if (variableDelegate instanceof ODTUsageVariableDelegate) {
                    Variable declared = variable.getDeclared();
                    if (!(declared instanceof ODTVariable) || declared.isParameter() || isAssignee(variable)) {
                        return;
                    }
                    ODTScriptLine scriptLine = PsiTreeUtil.getParentOfType(
                            variable,
                            ODTScriptLine.class);
                    List<ODTVariable> variablesFromAssignments =
                            ((ODTUsageVariableDelegate) variableDelegate).getVariablesFromAssignments(scriptLine, (PsiElement) declared);
                    if (variablesFromAssignments.isEmpty()) {
                        holder.registerProblem(
                                variable,
                                variable.getName() + " is used before it is assigned a value",
                                WARNING);
                    }
                }
            }

            private boolean isAssignee(ODTVariable variable) {
                return Optional.ofNullable(PsiTreeUtil.getParentOfType(variable, ODTAssignmentStatement.class))
                        .map(ODTAssignmentStatement::getQuery)
                        .map(odtQuery -> PsiTreeUtil.isContextAncestor(odtQuery, variable, true))
                        .orElse(false);
            }

        };
    }
}
