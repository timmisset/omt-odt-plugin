package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class OMTPredicateQueryType extends OMTQueryMetaType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream().allMatch(OppModel.INSTANCE::isPredicate)) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to predicates");
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (predicates)";
    }
}
