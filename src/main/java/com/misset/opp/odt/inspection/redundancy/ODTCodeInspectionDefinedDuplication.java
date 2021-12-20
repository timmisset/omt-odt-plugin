package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
                    final ODTDefineStatement defineStatement = (ODTDefineStatement) element;
                    final PsiFile file = holder.getFile();
                    // within this ODT File
                    if (PsiTreeUtil.findChildrenOfType(file, ODTDefineStatement.class).stream()
                            .filter(result -> result != defineStatement)
                            .anyMatch(result -> result.getCallId().equals(defineStatement.getCallId()))) {
                        holder.registerProblem(defineStatement.getDefineName(), WARNING_MESSAGE_DUPLICATION, ProblemHighlightType.WARNING);
                    }

                    if (file instanceof ODTFile) {
                        List<PsiCallable> callables = ((ODTFile) file).resolveInOMT(
                                        OMTCallableProvider.class,
                                        OMTCallableProvider.KEY,
                                        defineStatement.getCallId(),
                                        (provider, mapping) -> provider.getCallableMap(mapping, ((ODTFile) file).getHost()))
                                .orElse(Collections.emptyList());
                        // Either declared in the OMT file or imported:
                        if (callables.size() > 0) {
                            if (Stream.of(callables)
                                    .anyMatch(match -> match != element)) {
                                holder.registerProblem(defineStatement.getDefineName(), WARNING_MESSAGE_SHADOW, ProblemHighlightType.WARNING);
                            }
                        }
                    }
                }
            }
        };
    }
}
