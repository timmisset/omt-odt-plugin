package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.ForEachCommand;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.ODTAssignmentStatementImpl;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.CallableType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Code inspection for the specific Builtin Command ForEach which requires detailed information about the Call, rather than the
 * Callable which is why it is handled in the OMT language.
 * <p>
 * Basic validation on call signature (number of arguments etc.) is already handled by the Builtin Command
 */
public class ODTCommandInspectionForEach extends LocalInspectionTool {
    protected static final String CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH = "You should only call other ODT DEFINE COMMANDS/QUERIES or Builtin Commands/Operators from ForEach";
    protected static final String ALL_VALUES_ARE_TREATED_EQUAL = "Unnecessary usage of FOREACH, all values are treated equal. Assign values to the entire collection directly.";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates Builtin Command: @FOREACH for proper usage";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    if (((ODTCall) element).getCallable() != ForEachCommand.INSTANCE) {
                        return;
                    }
                    inspectForEach(holder, (ODTCall) element);
                }
            }
        };
    }

    private void inspectForEach(@NotNull ProblemsHolder holder,
                                @NotNull ODTCall call) {
        if (call.getSignature() != null) {
            final List<ODTSignatureArgument> signatureArgumentList = call.getSignature().getSignatureArgumentList();
            if (signatureArgumentList.size() == 2 && signatureArgumentList.get(1).getCommandBlock() != null) {
                final ODTCommandBlock commandBlock = signatureArgumentList.get(1).getCommandBlock();

                // validate that no illegal calls are made
                PsiTreeUtil.findChildrenOfType(commandBlock, ODTCall.class).stream()
                        .filter(this::illegalNestedCall)
                        .forEach(
                                nestedCall -> holder.registerProblem(nestedCall,
                                        CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH,
                                        ProblemHighlightType.ERROR)
                        );

                // validate that FOREACH is required
                final boolean unnecessaryUsage = PsiTreeUtil.findChildrenOfType(commandBlock, ODTVariable.class)
                        .stream()
                        .filter(variable -> variable.getName() != null && variable.getName().equals("$value"))
                        .noneMatch(variable -> isAssigningToVariable(variable) ||
                                isAssigningToStatement(variable) ||
                                isEncapsulatedByLogicalBlockOrCall(variable, call));
                if (unnecessaryUsage) {
                    holder.registerProblem(call, ALL_VALUES_ARE_TREATED_EQUAL, ProblemHighlightType.WARNING);
                }
            }
        }
    }

    private boolean illegalNestedCall(@NotNull ODTCall call) {
        // cannot call a procedure, activity etc. anything that is part of the OMT structure
        CallableType type = call.getCallable().getType();
        return !Set.of(CallableType.BUILTIN_COMMAND, CallableType.BUILTIN_OPERATOR, CallableType.DEFINE_QUERY, CallableType.DEFINE_COMMAND)
                .contains(type);
    }

    private boolean isEncapsulatedByLogicalBlockOrCall(@NotNull ODTVariable variable,
                                                       @NotNull ODTCall call) {
        return PsiTreeUtil.getParentOfType(variable, ODTLogicalBlock.class, ODTCommandCall.class) != call;
    }

    private boolean isAssigningToVariable(@NotNull ODTVariable variable) {
        ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(variable, ODTVariableAssignment.class);
        return variableAssignment != null && !variableAssignment.getVariableList().contains(variable);
    }

    private boolean isAssigningToStatement(@NotNull ODTVariable variable) {
        ODTAssignmentStatementImpl statement = PsiTreeUtil.getParentOfType(variable, ODTAssignmentStatementImpl.class);
        return statement != null && PsiTreeUtil.isAncestor(statement.getQuery(), variable, true);
    }
}
