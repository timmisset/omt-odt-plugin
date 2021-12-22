package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.references.OMTParamTypeReference;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import static com.misset.opp.omt.meta.model.variables.OMTParamMetaType.SHORTHAND_TYPED;

/**
 * Serves as a proxy between the OMTNamedVariableMetaType types and the PsiElement.
 * It acts as a wrapper element for the YamlPlainTextImpl (Scalar) to provide certain features
 * required by the Variable interface.
 * <p>
 * The MetaTypes are Psi-less classes which require to be called with the actual PsiElement they
 * represent in order to provide the information
 */
public class OMTYamlVariableDelegate extends YAMLPlainTextImpl implements OMTVariable, OMTYamlDelegate {
    public static final Key<Boolean> IS_PARAMETER = new Key<>("IS_OMT_PARAMETER");
    YAMLPlainTextImpl value;

    public OMTYamlVariableDelegate(@NotNull YAMLPlainTextImpl yamlValue, boolean isParameter) {
        super(yamlValue.getNode());
        this.value = yamlValue;
        IS_PARAMETER.set(value, isParameter);
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
    public PsiElement setName(@NotNull String newName) {
        newName = !newName.startsWith("$") ? "$" + newName : newName;
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
    public @NotNull Set<OntResource> resolve() {
        return getFromMeta(OMTNamedVariableMetaType::getType, Collections.emptySet());
    }

    @Override
    public boolean isParameter() {
        return Optional.ofNullable(IS_PARAMETER)
                .map(booleanKey -> booleanKey.get(getOriginalElement()))
                .orElse(false);
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

    @Override
    public PsiReference getReference() {
        if (!isParameter()) {
            return null;
        }
        final TextRange typePrefixRange = getTypePrefixRange(this);
        if (typePrefixRange != null) {
            return new OMTParamTypeReference(value, typePrefixRange);
        }
        return null;
    }

    private @Nullable TextRange getTypePrefixRange(YAMLPlainTextImpl value) {
        final Matcher matcher = SHORTHAND_TYPED.matcher(value.getText());
        final boolean b = matcher.find();
        if (b && matcher.groupCount() == 4 && matcher.start(3) > -1) {
            return new TextRange(matcher.start(3), matcher.end(3));
        }
        return null;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }
}
