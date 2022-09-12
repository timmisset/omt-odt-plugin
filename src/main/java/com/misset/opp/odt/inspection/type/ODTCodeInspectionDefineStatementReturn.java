package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.odt.psi.impl.ODTDefineCommandStatementImpl;
import com.misset.opp.odt.psi.impl.ODTDefineQueryStatementImplAbstract;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionDefineStatementReturn extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates the specified @return type with the calculated return type";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTDefineQueryStatementImplAbstract) {
                    inspectODTDefineQueryStatement(holder, (ODTDefineQueryStatementImplAbstract) element);
                } else if (element instanceof ODTDefineCommandStatementImpl) {
                    inspectODTDefineCommandStatement(holder, (ODTDefineCommandStatementImpl) element);
                }
            }
        };
    }

    private void inspectODTDefineQueryStatement(@NotNull ProblemsHolder holder,
                                                @NotNull ODTDefineQueryStatementImplAbstract defineQueryStatement) {
        Set<OntResource> resolve = defineQueryStatement.getQuery().resolve();
        Set<OntResource> returnType = defineQueryStatement.getReturnType();
        if (returnType == null) {
            return;
        }
        TTLValidationUtil.validateCompatibleTypes(
                returnType, resolve, holder, defineQueryStatement
        );

        if (returnType.contains(OppModelConstants.getVoidResponse())) {
            holder.registerProblem(defineQueryStatement,
                    "A query should not be typed void",
                    ProblemHighlightType.WARNING);
        }
    }

    private void inspectODTDefineCommandStatement(@NotNull ProblemsHolder holder,
                                                  @NotNull ODTDefineCommandStatementImpl defineCommandStatement) {
        Set<OntResource> returnType = defineCommandStatement.getReturnType();
        if (returnType == null) {
            return;
        }

        Collection<ODTReturnStatement> returnStatements = PsiTreeUtil.findChildrenOfType(defineCommandStatement, ODTReturnStatement.class);

        for (ODTReturnStatement returnStatement : returnStatements) {
            Set<OntResource> resources = Optional.ofNullable(returnStatement.getResolvableValue())
                    .map(Resolvable::resolve)
                    .orElse(Collections.emptySet());
            TTLValidationUtil.validateCompatibleTypes(
                    returnType, resources, holder, returnStatement
            );

            if (returnType.contains(OppModelConstants.getVoidResponse())) {
                holder.registerProblem(returnStatement,
                        "A void command should not contain RETURN statements",
                        ProblemHighlightType.WARNING);
            }
        }
    }
}
