package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyClassType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTOntologyClassesArrayType extends YamlArrayType {
    public OMTOntologyClassesArrayType() {
        super(new OMTOntologyClassType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // TODO: validate distinct ontology classes by their id
    }
}
