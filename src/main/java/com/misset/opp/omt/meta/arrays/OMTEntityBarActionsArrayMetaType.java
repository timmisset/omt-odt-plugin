package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.actions.OMTEntityBarActionMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTEntityBarActionsArrayMetaType extends YamlArrayType {
    public OMTEntityBarActionsArrayMetaType() {
        super(new OMTEntityBarActionMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // TODO: validate distinct actions by their id
    }
}
