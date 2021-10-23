package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTActionsArrayMetaType extends YamlArrayType {
    public OMTActionsArrayMetaType() {
        super(new OMTActionMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // TODO: validate distinct actions by their id
    }
}
