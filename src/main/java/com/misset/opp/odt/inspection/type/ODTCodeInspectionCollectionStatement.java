package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.psi.ODTCollectionStatement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                    inspect((ODTCollectionStatement) element);
                }
            }

            private void inspect(ODTCollectionStatement collectionStatement) {
                final ODTQuery query = collectionStatement.getQuery();
                final ODTResolvableValue resolvableValue = collectionStatement.getResolvableValue();
                OntologyValidationUtil.getInstance(holder.getProject()).validateCompatibleTypes(query.resolve(),
                        resolvableValue.resolve(),
                        holder,
                        resolvableValue);
            }
        };
    }
}
