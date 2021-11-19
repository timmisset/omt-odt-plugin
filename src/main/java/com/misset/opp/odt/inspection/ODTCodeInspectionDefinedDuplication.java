package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTCodeInspectionDefinedDuplication extends LocalInspectionTool {

    protected static final String WARNING_MESSAGE_DUPLICATION = "Duplication";
    protected static final String WARNING_MESSAGE_SHADOW = "Shadowing existing item in the OMT model (either declared or imported)";

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
                if (element instanceof ODTDefineStatement) {
                    final ODTDefineStatement statement = (ODTDefineStatement) element;
                    final PsiFile file = holder.getFile();
                    // within this ODT File
                    if (PsiTreeUtil.findChildrenOfType(file, ODTDefineStatement.class).stream()
                            .filter(defineStatement -> defineStatement != statement)
                            .anyMatch(statement::hasSameIdentifier)) {
                        holder.registerProblem(statement, WARNING_MESSAGE_DUPLICATION, ProblemHighlightType.WARNING);
                    }

                    if (file instanceof ODTFile) {
                        final ResolveResult[] resolveResults = ((ODTFile) file).resolveInOMT(
                                        OMTCallableProvider.class,
                                        statement.getCallId(),
                                        OMTCallableProvider::getCallableMap)
                                .orElse(ResolveResult.EMPTY_ARRAY);
                        // Either declared in the OMT file or imported:
                        if (resolveResults.length > 0) {
                            holder.registerProblem(statement, WARNING_MESSAGE_SHADOW, ProblemHighlightType.WARNING);
                        }
                    }
                }
            }
        };
    }
}
