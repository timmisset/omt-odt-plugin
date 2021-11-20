package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.model.ODTSimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

/**
 * The Meta-type for a OMTParamMetaType
 */
@ODTSimpleInjectable
public class OMTParamTypeType extends YamlScalarType implements ODTInjectable {

    public OMTParamTypeType() {
        super("OMTParamTypeType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {

    }
}
