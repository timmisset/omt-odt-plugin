package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.ForEachCommand;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.omt.psi.impl.OMTCallableImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Code inspection for the specific Builtin Command ForEach which requires detailed information about the Call, rather than the
 * Callable which is why it is handled in the OMT language.
 * <p>
 * Basic validation on call signature (number of arguments etc.) is already handled by the Builtin Command
 */
public class ODTCommandInspectionForEach extends LocalInspectionTool {
    protected static final String CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH = "Cannot call a Procedure within a ForEach command";
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
                        .noneMatch(variable -> isAssigningToVariable(variable) || isEncapsulatedByLogicalBlockOrCall(variable, call));
                if (unnecessaryUsage) {
                    holder.registerProblem(call, ALL_VALUES_ARE_TREATED_EQUAL, ProblemHighlightType.WARNING);
                }
            }
        }
    }

    private boolean illegalNestedCall(@NotNull ODTCall call) {
        // cannot call a procedure, activity etc. anything that is part of the OMT structure
        return call.getCallable() instanceof OMTCallableImpl;
    }

    private boolean isEncapsulatedByLogicalBlockOrCall(@NotNull ODTVariable variable,
                                                       @NotNull ODTCall call) {
        return PsiTreeUtil.getParentOfType(variable, ODTLogicalBlock.class, ODTCommandCall.class) != call;
    }

    private boolean isAssigningToVariable(@NotNull ODTVariable variable) {
        ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(variable, ODTVariableAssignment.class);
        return variableAssignment != null && !variableAssignment.getVariableList().contains(variable);
    }
}
