package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

public class OMTRuleMetaType extends YamlScalarType {
    public OMTRuleMetaType() {
        super("OMTRule");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        // TODO: validate that a rule should point to a valid ODT query returning a boolean
        super.validateScalarValue(scalarValue, holder);
    }

}
