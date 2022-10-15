package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
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
                        OntologyModel.getInstance(holder.getProject()).isIndividual(resource) &&
                        Optional.ofNullable(OntologyModel.getInstance(holder.getProject()).toClass(resource))
                                .map(OntologyModelConstants.getNamedGraphClass()::equals)
                                .orElse(false))) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a graph");
    }
}
