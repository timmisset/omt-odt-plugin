package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTCollectionStatement;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.ResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionCollectionStatement extends LocalInspectionTool {
    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates that assignments have equal or compatible RDF types";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCollectionStatement) {
                    final ODTCollectionStatement collectionStatement = (ODTCollectionStatement) element;
                    final Set<OntResource> query = (collectionStatement).getQuery().resolve();
                    final Set<OntResource> resolvableValue = (collectionStatement).getResolvableValue().resolve();
                    if (!query.isEmpty() &&
                            !resolvableValue.isEmpty() &&
                            !OppModel.INSTANCE.areCompatible(query, resolvableValue)) {
                        holder.registerProblem(collectionStatement.getResolvableValue(),
                                createWarning(query, resolvableValue),
                                ProblemHighlightType.WARNING);
                    }
                }
            }
        };
    }

    private String createWarning(Set<OntResource> query,
                                 Set<OntResource> resolvableValue) {
        return "Incompatible types:" + "\n" +
                "cannot assign " + "\n" +
                ResourceUtil.describeUrisJoined(resolvableValue) + "\n" +
                "to" + "\n" +
                ResourceUtil.describeUrisJoined(query);
    }
}
