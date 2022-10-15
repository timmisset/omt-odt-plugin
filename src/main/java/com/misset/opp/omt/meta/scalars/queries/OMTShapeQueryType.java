package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.util.OntologyValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Set;

public class OMTShapeQueryType extends OMTQueryMetaType {
    private static final OMTShapeQueryType INSTANCE = new OMTShapeQueryType();

    public static OMTShapeQueryType getInstance() {
        return INSTANCE;
    }

    private OMTShapeQueryType() {
        super();
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Set<OntResource> resolve = resolve(scalarValue);
        if (resolve.isEmpty() || resolve.stream().allMatch(OntologyValidationUtil.getInstance(holder.getProject())::isGraphshapeInstance)) {
            return;
        }
        holder.registerProblem(scalarValue, "Expected a query that resolves to a GraphShape");
    }

}
