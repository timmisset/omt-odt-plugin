package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

public class OMTReasonMetaType extends YamlScalarType {
    public OMTReasonMetaType() {
        super("Reason");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        // TODO: Validate against known reasons
        super.validateScalarValue(scalarValue, holder);
    }
}
