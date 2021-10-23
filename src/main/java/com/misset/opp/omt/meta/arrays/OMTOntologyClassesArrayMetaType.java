package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyClassMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTOntologyClassesArrayMetaType extends YamlArrayType {
    public OMTOntologyClassesArrayMetaType() {
        super(new OMTOntologyClassMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // TODO: validate distinct ontology classes by their id
    }
}
