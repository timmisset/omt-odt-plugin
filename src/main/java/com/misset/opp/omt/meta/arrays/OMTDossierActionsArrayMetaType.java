package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.actions.OMTDossierActionMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTDossierActionsArrayMetaType extends YamlArrayType {
    public OMTDossierActionsArrayMetaType() {
        super(new OMTDossierActionMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // TODO: validate distinct actions by their id
    }
}
