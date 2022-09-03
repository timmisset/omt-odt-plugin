package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTCodeInspectionDefinedDuplication extends LocalInspectionTool {

    protected static final String WARNING_MESSAGE_DUPLICATION = "Duplication";

    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Validate that DEFINE COMMAND and DEFINE QUERIES are not duplicated (by name)";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTDefineStatement && holder.getFile() instanceof ODTFile) {
                    ODTFile file = (ODTFile) holder.getFile();
                    final ODTDefineStatement defineStatement = (ODTDefineStatement) element;

                    boolean isDuplicated = file.getCallables(defineStatement.getCallId()).stream()
                            .anyMatch(psiCallable -> psiCallable != defineStatement);
                    if (isDuplicated) {
                        holder.registerProblem(defineStatement.getDefineName(),
                                WARNING_MESSAGE_DUPLICATION,
                                ProblemHighlightType.WARNING);
                    }
                }
            }
        };
    }
}
