package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLScalar;

public class ODTBooleanQueryType extends ODTQueryType {

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        // TODO: validate that the value resolves to a boolean
    }

    @Override
    public @NotNull String getDisplayName() {
        return super.getDisplayName() + " (boolean)";
    }
}
