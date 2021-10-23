package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTServiceMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTServicesArrayMetaType extends YamlArrayType {
    public OMTServicesArrayMetaType() {
        super(new OMTServiceMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {

    }
}
