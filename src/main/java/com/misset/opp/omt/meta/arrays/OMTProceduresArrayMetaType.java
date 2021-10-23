package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTProcedureMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTProceduresArrayMetaType extends YamlArrayType {
    public OMTProceduresArrayMetaType() {
        super(new OMTProcedureMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {

    }
}
