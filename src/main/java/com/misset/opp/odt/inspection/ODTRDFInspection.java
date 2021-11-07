package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Due to the nature of the LocalInspectionTool running on many threads,
 * we need to avoid CCMEs by not using the Ontology with any operations that might affect
 * the RDF nodes collection (such as: OntResource::asClass, asIndividual etc);
 */
public class ODTRDFInspection extends LocalInspectionTool {
    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect RDF statements";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {

        return new PsiElementVisitor() {

            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTInspectedElement) {
                    ((ODTInspectedElement) element).inspect(holder);
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
        Logger.getInstance(ODTRDFInspection.class)
                .warn("Inspection took: " + Duration.between(dateTime, LocalDateTime.now()).toMillis() + " ms");
    }
}
