package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class OMTShapeQueryType extends OMTQueryMetaType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream().allMatch(TTLValidationUtil::isGraphshapeInstance)) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a GraphShape");
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        // this makes sure the injected fragment shows only applicable completions (i.e. graphshapes)
        return InjectableContentType.GraphShapeQuery;
    }
}
