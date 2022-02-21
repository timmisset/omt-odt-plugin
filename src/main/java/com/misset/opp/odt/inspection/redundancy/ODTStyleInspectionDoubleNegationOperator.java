package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.builtin.operators.EmptyOperator;
import com.misset.opp.odt.builtin.operators.ExistsOperator;
import com.misset.opp.odt.builtin.operators.NotOperator;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.jetbrains.annotations.NotNull;

/**
 * Code inspection for all unused declarations
 */
public class ODTStyleInspectionDoubleNegationOperator extends LocalInspectionTool {

    protected static final String WARNING = "Unnecessary (double) negation";
    protected static final String REPLACE = "Replace with positive assertion";

    private enum CodeStyle {
        LEADING_NEGATION,       // $variable / NOT EXISTS
        INSIDE_NEGATION,        // NOT($variable / EXISTS)
        TRAILING_NEGATION       // $variable / EXISTS / NOT
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTOperatorCall) {
                    inspectOperatorCall(holder, (ODTOperatorCall) element);
                }
            }
        };
    }

    private void inspectOperatorCall(@NotNull ProblemsHolder holder,
                                     @NotNull ODTOperatorCall operatorCall) {
        String name = operatorCall.getName();
        if (name.equals(ExistsOperator.INSTANCE.getName()) || name.equals(EmptyOperator.INSTANCE.getName())) {
            ODTQueryOperationStep queryOperationStep = PsiTreeUtil.getParentOfType(operatorCall, ODTQueryOperationStep.class);
            if (queryOperationStep == null) {
                return;
            }
            // LEADING_NEGATION
            if (PsiTreeUtil.getParentOfType(queryOperationStep, ODTNegatedStep.class, ODTQueryOperationStep.class) instanceof ODTNegatedStep) {
                holder.registerProblem(operatorCall, WARNING, ProblemHighlightType.WEAK_WARNING, getQuickFix(CodeStyle.LEADING_NEGATION));
                return;
            }

            // INSIDE_NEGATION
            ODTResolvable parentOfType = PsiTreeUtil.getParentOfType(queryOperationStep, ODTScript.class, ODTCall.class);
            if (parentOfType instanceof ODTCall && ((ODTCall) parentOfType).getName().equals(NotOperator.INSTANCE.getName())) {
                holder.registerProblem(operatorCall, WARNING, ProblemHighlightType.WEAK_WARNING, getQuickFix(CodeStyle.INSIDE_NEGATION));
                return;
            }

            // TRAILING_NEGATION
            ODTQueryOperationStep siblingOperationStep = PsiTreeUtil.getNextSiblingOfType(queryOperationStep, ODTQueryOperationStep.class);
            if (siblingOperationStep == null) {
                return;
            }
            PsiElement leafElement = PsiTreeUtil.getDeepestFirst(siblingOperationStep);
            if (PsiUtilCore.getElementType(leafElement) == ODTTypes.NOT_OPERATOR) {
                holder.registerProblem(operatorCall, WARNING, ProblemHighlightType.WEAK_WARNING, getQuickFix(CodeStyle.TRAILING_NEGATION));
            }
        }
    }

    private LocalQuickFix getQuickFix(CodeStyle codeStyle) {

        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return REPLACE;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                PsiElement psiElement = descriptor.getPsiElement();
                if (psiElement instanceof ODTOperatorCall) {
                    ODTOperatorCall call = (ODTOperatorCall) psiElement;
                    if (codeStyle == CodeStyle.INSIDE_NEGATION) {
                        replaceOperator(project, call);
                    } else if (codeStyle == CodeStyle.LEADING_NEGATION) {
                        replaceKeyword(project, call);
                    } else if (codeStyle == CodeStyle.TRAILING_NEGATION) {
                        removeTrailing(project, call);
                    }

                }
            }

            private void removeTrailing(Project project, ODTOperatorCall call) {
                ODTCall oppositeCall = getOppositeCall(project, call);
                if (oppositeCall == null) {
                    return;
                }
                PsiElement replacedCall = call.replace(oppositeCall);
                PsiElement psiElement = PsiTreeUtil.nextVisibleLeaf(replacedCall);
                if (psiElement != null && PsiUtilCore.getElementType(psiElement) == ODTTypes.FORWARD_SLASH) {
                    psiElement.delete();
                    psiElement = PsiTreeUtil.nextVisibleLeaf(replacedCall);
                }
                if (psiElement != null && PsiUtilCore.getElementType(psiElement) == ODTTypes.NOT_OPERATOR) {
                    psiElement.delete();
                }

                // the white-space formatting is now off, the easiest fix is it to replace the parent script with itself
                // which will trigger a reformatting
                ODTScriptLine scriptLine = PsiTreeUtil.getParentOfType(replacedCall, ODTScriptLine.class);
                if (scriptLine != null) {
                    ODTScriptLine replacementScript = ODTElementGenerator.getInstance(project).fromFile(scriptLine.getText(), ODTScriptLine.class);
                    scriptLine.replace(replacementScript);
                }
            }

            private void replaceKeyword(Project project, ODTOperatorCall call) {
                ODTNegatedStep negatedStep = PsiTreeUtil.getParentOfType(call, ODTNegatedStep.class);
                ODTCall oppositeCall = getOppositeCall(project, call);
                if (negatedStep == null || oppositeCall == null) {
                    return;
                }
                negatedStep.replace(oppositeCall);
            }

            private void replaceOperator(Project project, ODTOperatorCall call) {
                ODTCall oppositeCall = getOppositeCall(project, call);
                if (oppositeCall == null) {
                    return;
                }
                PsiElement replacedCall = call.replace(oppositeCall);
                ODTCall notCall = PsiTreeUtil.getParentOfType(replacedCall, ODTCall.class);
                ODTSignatureArgument signatureArgument = PsiTreeUtil.getParentOfType(replacedCall, ODTSignatureArgument.class);

                if (notCall != null && signatureArgument != null && notCall.getName().equals(NotOperator.INSTANCE.getName())) {
                    notCall.replace(signatureArgument);
                } else {
                    // something is wrong, restore:
                    replacedCall.replace(call);
                }
            }

            private ODTCall getOppositeCall(Project project, ODTCall call) {
                String newName = getNewName(call);
                return ODTElementGenerator.getInstance(project).createCall(newName, null);
            }

            private String getNewName(ODTCall call) {
                if (call.getName().equals(ExistsOperator.INSTANCE.getName())) {
                    return EmptyOperator.INSTANCE.getName();
                } else {
                    return ExistsOperator.INSTANCE.getName();
                }
            }
        };
    }
}
