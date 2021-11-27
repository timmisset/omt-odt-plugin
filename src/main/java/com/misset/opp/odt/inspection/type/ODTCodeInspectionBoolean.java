package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.odt.psi.ODTIfBlock;
import com.misset.opp.ttl.validation.TTLValidationUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Code inspection for all unused declarations
 */
public class ODTCodeInspectionBoolean extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates that queries and variables resolve to a boolean when required";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new ModelAwarePsiElementVisitor() {
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
                        TTLValidationUtil.validateBoolean(query.resolve(), holder, query)
        );
    }

    private void inspectIfBlock(@NotNull ProblemsHolder holder,
                                @NotNull ODTIfBlock ifBlock) {
        TTLValidationUtil.validateBoolean(ifBlock.getQuery().resolve(), holder, ifBlock.getQuery());
    }
}
