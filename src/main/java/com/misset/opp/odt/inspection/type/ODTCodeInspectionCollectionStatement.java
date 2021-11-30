package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.ODTAddToCollection;
import com.misset.opp.odt.psi.ODTCollectionStatement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTRemoveFromCollection;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
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
        return "Validates that assignments have equal or compatible RDF types and checks the modularity";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new ModelAwarePsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCollectionStatement) {
                    inspect((ODTCollectionStatement) element);
                }
            }

            private void inspect(ODTCollectionStatement collectionStatement) {
                final ODTQuery query = collectionStatement.getQuery();
                final ODTResolvableValue resolvableValue = collectionStatement.getResolvableValue();
                TTLValidationUtil.validateCompatibleTypes(query.resolve(),
                        resolvableValue.resolve(),
                        holder,
                        resolvableValue);

                if (query instanceof ODTResolvableQueryPath) {
                    inspectModularity(collectionStatement, (ODTResolvableQueryPath) query, resolvableValue);
                }
            }

            private void inspectModularity(ODTCollectionStatement collectionStatement,
                                           ODTResolvableQueryPath query,
                                           ODTResolvableValue resolvableValue) {
                final Pair<Set<OntResource>, Property> subjectPredicate = query.resolveToSubjectPredicate();
                if (subjectPredicate == null) {
                    return;
                }

                if (collectionStatement instanceof ODTAddToCollection ||
                        collectionStatement instanceof ODTRemoveFromCollection ||
                        resolvableValue.isMultiple()) {
                    TTLValidationUtil.validateModularityMultiple(subjectPredicate.getFirst(),
                            subjectPredicate.getSecond(),
                            holder,
                            resolvableValue);
                }
            }

        };
    }
}
