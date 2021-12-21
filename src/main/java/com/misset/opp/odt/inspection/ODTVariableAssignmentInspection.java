package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ODTVariableAssignmentInspection extends LocalInspectionTool {

    protected static final String WARNING_NO_SECOND_ARGUMENT = "Call doesn't return a second argument. " +
            "If you're expecting an array of values you must use extract them using $response[0], $response[1] etc.";

    protected static final String ERROR_READ_ONLY = "Variable is readonly";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspects if variable assignments are valid";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTVariable) {
                    ODTVariable variable = (ODTVariable) element;
                    if (variable.isAssignedVariable()) {
                        inspectVariable(variable, holder);
                        ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(variable, ODTVariableAssignment.class);
                        if (variableAssignment == null) {
                            return;
                        }
                        inspectVariableAssignment(variableAssignment, variable, holder);
                    }
                }
            }

            private void inspectVariable(ODTVariable variable, ProblemsHolder holder) {
                if (variable.isReadonly() || variable.isGlobal()) {
                    holder.registerProblem(variable, ERROR_READ_ONLY, ProblemHighlightType.ERROR);
                }
            }

            private void inspectVariableAssignment(ODTVariableAssignment variableAssignment, ODTVariable variable, ProblemsHolder holder) {
                int position = variableAssignment.getVariableList().indexOf(variable);
                if (position == 1) {
                    // second variable, check if possible:
                    if (!inspectCallHasSecondArgumentResponse(variableAssignment, variable)) {
                        holder.registerProblem(variable, WARNING_NO_SECOND_ARGUMENT, ProblemHighlightType.WARNING);
                    }
                }
            }

            private boolean inspectCallHasSecondArgumentResponse(ODTVariableAssignment variableAssignment, ODTVariable variable) {
                return Optional.ofNullable(variableAssignment.getVariableValue().getCommandCall())
                        .map(ODTCall::getCallable)
                        .map(Callable::getSecondReturnArgument)
                        .map(resources -> !resources.isEmpty())
                        .orElse(false);
            }

        };
    }
}
