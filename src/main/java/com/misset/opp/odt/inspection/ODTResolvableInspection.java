package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Inspection of all elements that implement the ODTResolvable interface
 */
public class ODTResolvableInspection extends LocalInspectionTool {
    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect ODT Resolvable elements<br>" +
                "Resolvable elements are parts of the ODT language that can be resolved to a set of RDF resource types.<br>" +
                "<br>" +
                "Examples of these steps are:<br>" +
                "- Curie (ont:Class)<br>" +
                "- Calls to Operators / Queries<br>" +
                "- Runnables such as Procedures, Activities etc";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {

        return new PsiElementVisitor() {

            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTResolvable) {
                    ((ODTResolvable) element).inspect(holder);
                }
            }
        };
    }

    private LocalDateTime dateTime;

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session,
                                  boolean isOnTheFly) {
        dateTime = LocalDateTime.now();
    }

    @Override
    public void inspectionFinished(@NotNull LocalInspectionToolSession session,
                                   @NotNull ProblemsHolder problemsHolder) {
        Logger.getInstance(ODTResolvableInspection.class)
                .warn("Inspection took: " + Duration.between(dateTime, LocalDateTime.now()).toMillis() + " ms");
    }
}
