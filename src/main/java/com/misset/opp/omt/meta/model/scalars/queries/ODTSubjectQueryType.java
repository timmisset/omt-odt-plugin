package com.misset.opp.omt.meta.model.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class ODTSubjectQueryType extends ODTQueryMetaType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream().allMatch(OntResource::isIndividual)) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to Subjects");
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (subjects)";
    }
}
