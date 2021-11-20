package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCommandBlock;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.odt.psi.ODTScriptLine;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionUnreachable extends LocalInspectionTool {

    protected static final String WARNING_MESSAGE = "Unreachable code";

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
                if (element instanceof ODTScriptLine) {
                    inspectScriptLine(holder, (ODTScriptLine) element);
                }
            }
        };
    }

    private void inspectScriptLine(@NotNull ProblemsHolder holder,
                                   @NotNull ODTScriptLine scriptLine) {
        ODTScriptLine previousScriptline = PsiTreeUtil.getPrevSiblingOfType(scriptLine, ODTScriptLine.class);
        while (previousScriptline != null) {
            if (PsiTreeUtil.findChildOfType(previousScriptline,
                    ODTReturnStatement.class,
                    true,
                    ODTCommandBlock.class) != null) {
                holder.registerProblem(scriptLine, WARNING_MESSAGE, ProblemHighlightType.WARNING);
                return;
            }
            previousScriptline = PsiTreeUtil.getPrevSiblingOfType(previousScriptline, ODTScriptLine.class);
        }
    }
}
