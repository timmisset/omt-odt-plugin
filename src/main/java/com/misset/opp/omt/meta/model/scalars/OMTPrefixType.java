package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.List;
import java.util.Set;

public class OMTPrefixType extends YamlScalarType {

    public OMTPrefixType() {
        super("OMTPrefix");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        // TODO: validate value for syntax validity
        super.validateScalarValue(scalarValue, holder);
    }

}
