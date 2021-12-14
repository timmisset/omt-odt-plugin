package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.psi.OMTVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Serves as a proxy between the OMTNamedVariableMetaType types and the PsiElement.
 * It acts as a wrapper element for the YamlPlainTextImpl (Scalar) to provide certain features
 * required by the Variable interface.
 * <p>
 * The MetaTypes are Psi-less classes which require to be called with the actual PsiElement they
 * represent in order to provide the information
 */
public class OMTYamlVariableDelegate extends YAMLPlainTextImpl implements OMTVariable, OMTYamlDelegate {
    YAMLValue value;

    public OMTYamlVariableDelegate(@NotNull YAMLValue yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getDescription() {
        return getFromMeta(OMTNamedVariableMetaType::getDescription, value.getText());
    }

    @Override
    public String getName() {
        return getFromMeta(OMTNamedVariableMetaType::getName, value.getText());
    }

    @Override
    public PsiElement setName(String newName) {
        final TextRange textRange = (TextRange) getFromMeta(OMTNamedVariableMetaType::getNameTextRange,
                value.getText());
        final String renamed = textRange.replace(value.getText(), newName);
        final YAMLKeyValue foo = YAMLElementGenerator.getInstance(value.getProject())
                .createYamlKeyValue("foo", renamed);
        if (foo.getValue() != null) {
            return value.replace(foo.getValue());
        }
        return value;
    }

    @Override
    public Set<OntResource> getType() {
        return getFromMeta(OMTNamedVariableMetaType::getType, Collections.emptySet());
    }

    public TextRange getNameTextRange() {
        return getFromMeta(OMTNamedVariableMetaType::getNameTextRange, TextRange.allOf(getName()));
    }

    @Override
    public boolean isParameter() {
        return OMTVariableProvider.IS_PARAMETER.get(getOriginalElement());
    }

    @Override
    public PsiElement getOriginalElement() {
        return value;
    }

    private <T> T getFromMeta(BiFunction<OMTNamedVariableMetaType, YAMLValue, T> ifPresent,
                              T orElse) {
        if (value == null) {
            return orElse;
        }
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(value.getProject()).getValueMetaType(value))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTNamedVariableMetaType.class::isInstance)
                .map(OMTNamedVariableMetaType.class::cast)
                .map(omtNamedVariableMetaType -> ifPresent.apply(omtNamedVariableMetaType, value))
                .orElse(orElse);
    }

    @Override
    public boolean isReadonly() {
        return getFromMeta(OMTNamedVariableMetaType::isReadonly, false);
    }
}
