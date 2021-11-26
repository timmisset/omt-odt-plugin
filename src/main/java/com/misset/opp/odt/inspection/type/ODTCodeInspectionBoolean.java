package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.odt.psi.ODTIfBlock;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionBoolean extends LocalInspectionTool {

    protected static final String ERROR_MESSAGE = "Boolean type required";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates that queries and variables resolve to a boolean when required";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTBooleanStatement) {
                    inspectBooleanStatement(holder, (ODTBooleanStatement) element);
                } else if (element instanceof ODTIfBlock) {
                    inspectIfBlock(holder, (ODTIfBlock) element);
                }
            }
        };
    }

    private void inspectBooleanStatement(@NotNull ProblemsHolder holder,
                                         @NotNull ODTBooleanStatement booleanStatement) {
        booleanStatement.getQueryList().forEach(
                query ->
                        validateBoolean(holder, query)
        );
    }

    private void inspectIfBlock(@NotNull ProblemsHolder holder,
                                @NotNull ODTIfBlock ifBlock) {
        validateBoolean(holder, ifBlock.getQuery());
    }

    private void validateBoolean(@NotNull ProblemsHolder holder,
                                 ODTQuery query) {
        final Set<OntResource> resolve = query.resolve();
        if (!resolve.isEmpty() && !resolve.stream().allMatch(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE::equals)) {
            holder.registerProblem(query, ERROR_MESSAGE, ProblemHighlightType.ERROR);
        }
    }
}
