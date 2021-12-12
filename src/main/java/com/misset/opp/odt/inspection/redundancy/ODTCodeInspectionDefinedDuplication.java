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
import com.misset.opp.odt.psi.impl.callable.ODTResolvableDefineName;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
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
                if (element instanceof ODTResolvableDefineName) {
                    final ODTResolvableDefineName name = (ODTResolvableDefineName) element;
                    final PsiFile file = holder.getFile();
                    // within this ODT File
                    if (PsiTreeUtil.findChildrenOfType(file, ODTResolvableDefineName.class).stream()
                            .filter(defineName -> defineName != name)
                            .anyMatch(name::hasSameIdentifier)) {
                        holder.registerProblem(name, WARNING_MESSAGE_DUPLICATION, ProblemHighlightType.WARNING);
                    }

                    if (file instanceof ODTFile) {
                        final ResolveResult[] resolveResults = ((ODTFile) file).resolveInOMT(
                                        OMTCallableProvider.class,
                                        OMTCallableProvider.KEY,
                                        name.getCallId(),
                                        (provider, mapping) -> provider.getCallableMap(mapping, ((ODTFile) file).getHost()))
                                .orElse(ResolveResult.EMPTY_ARRAY);
                        // Either declared in the OMT file or imported:
                        if (resolveResults.length > 0) {
                            if (Stream.of(resolveResults).map(ResolveResult::getElement)
                                    .filter(Objects::nonNull)
                                    .anyMatch(match -> match != element)) {
                                holder.registerProblem(name, WARNING_MESSAGE_SHADOW, ProblemHighlightType.WARNING);
                            }
                        }
                    }
                }
            }
        };
    }
}
