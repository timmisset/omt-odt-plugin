package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Optional;
import java.util.Set;

public class OMTGraphQueryType extends OMTQueryMetaType {
    private static final OMTGraphQueryType INSTANCE = new OMTGraphQueryType();

    public static OMTGraphQueryType getInstance() {
        return INSTANCE;
    }

    private OMTGraphQueryType() {
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream()
                .allMatch(resource -> resource != null &&
                        OppModel.INSTANCE.isIndividual(resource) &&
                        Optional.ofNullable(OppModel.INSTANCE.toClass(resource))
                                .map(OppModelConstants.NAMED_GRAPH_CLASS::equals)
                                .orElse(false))) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a graph");
    }
}
