package com.misset.opp.omt.meta.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;

public class OMTIriMetaType extends YamlScalarType {

    public OMTIriMetaType() {
        super("OMT Iri");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        // TODO: validate value for syntax validity
        super.validateScalarValue(scalarValue, holder);
    }

    public static String getNamespace(YAMLValue value) {
        return Optional.ofNullable(value)
                .map(PsiElement::getText)
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(1, s.length() - 1))
                .orElse(null);
    }

}
