package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class OMTBooleanQueryType extends OMTQueryMetaType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream().allMatch(OppModelConstants.XSD_BOOLEAN_INSTANCE::equals)) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a boolean");
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (boolean)";
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.Boolean;
    }
}
