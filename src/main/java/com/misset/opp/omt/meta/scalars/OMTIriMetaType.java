package com.misset.opp.omt.meta.scalars;

import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;

public class OMTIriMetaType extends YamlScalarType {
    private static final OMTIriMetaType INSTANCE = new OMTIriMetaType();

    public static OMTIriMetaType getInstance() {
        return INSTANCE;
    }

    private OMTIriMetaType() {
        super("OMT Iri");
    }

    public static String getNamespace(YAMLValue value) {
        return Optional.ofNullable(value)
                .map(PsiElement::getText)
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(1, s.length() - 1))
                .orElse(null);
    }

}
