package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class OMTBooleanQueryType extends OMTQueryMetaType {
    private static final OMTBooleanQueryType INSTANCE = new OMTBooleanQueryType();

    public static OMTBooleanQueryType getInstance() {
        return INSTANCE;
    }

    private OMTBooleanQueryType() {
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.stream().anyMatch(
                resource -> !OntologyModel.getInstance(holder.getProject()).isInstanceOf(resource, OntologyModelConstants.getXsdBoolean())
        )) {
            holder.registerProblem(scalarValue, "Expected a query that resolves to a boolean");
        }
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (boolean)";
    }

}
