package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.resolvable.Callable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionUnreachable extends LocalInspectionTool {

    protected static final String UNREACHABLE_CODE = "Unreachable code";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Detects unreachable code";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTReturnStatement) {
                    inspectScriptline(holder, element);
                } else if (element instanceof ODTCommandCall) {
                    inspectCall(holder, (ODTCommandCall) element);
                }
            }
        };
    }

    private void inspectCall(@NotNull ProblemsHolder holder,
                             @NotNull ODTCommandCall commandCall) {
        Callable callable = commandCall.getCallable();
        if (callable != null && callable.isFinalCommand()) {
            inspectScriptline(holder, commandCall);
        }
    }

    private void inspectScriptline(@NotNull ProblemsHolder holder,
                                   @Nullable PsiElement element) {
        Optional.ofNullable(PsiTreeUtil.getParentOfType(element, ODTScriptLine.class))
                .ifPresent(scriptLine -> inspectScriptline(holder, scriptLine));
    }

    private void inspectScriptline(@NotNull ProblemsHolder holder,
                                   @NotNull ODTScriptLine scriptLine) {
        PsiTreeUtil.findChildrenOfType(scriptLine.getParent(), ODTScriptLine.class)
                .stream()
                .filter(sibling -> sibling.getTextOffset() > scriptLine.getTextOffset())
                .forEach(sibling -> holder.registerProblem(sibling, UNREACHABLE_CODE, ProblemHighlightType.WARNING));
    }
}
