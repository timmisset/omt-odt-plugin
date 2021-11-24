package com.misset.opp.omt.meta.model.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class ODTGraphQueryType extends ODTQueryMetaType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream()
                .allMatch(resource -> resource.isIndividual() && resource.asIndividual().getOntClass(true).equals(
                        OppModel.INSTANCE.NAMED_GRAPH_CLASS))) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a graph");
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (graph)";
    }
}
