package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTParamMetaType extends OMTMetaShorthandType implements OMTNamedVariableMetaType {

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("type", OMTParamTypeType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)\\s*(\\(([^)]+)\\))?$");
    private static final Pattern SHORTHAND_TYPED = Pattern.compile("^(\\$\\w+)\\s*(\\(([A-z0-9]+):([A-z0-9]+)\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (type)' OR '$name'";

    public OMTParamMetaType() {
        super("OMT Parameter");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Pattern getShorthandPattern() {
        return SHORTHAND;
    }

    @Override
    protected String getShorthandSyntaxError(YAMLValue value) {
        return SYNTAX_ERROR;
    }

    @Override
    public Set<OntResource> getType(YAMLValue value) {
        if(value instanceof YAMLMapping) {
            return getTypeFromDestructed((YAMLMapping) value);
        }

        final Matcher matcher = SHORTHAND.matcher(value.getText());
        final boolean b = matcher.find();
        if (!b || matcher.group(3) == null) {
            return null;
        } // no type provided
        return OMTParamTypeType.resolveType(value, matcher.group(3));
    }

    @Override
    public Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("type"))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTParamTypeType.resolveType(value, value.getText()))
                .orElse(Collections.emptySet());
    }

    @Override
    public String getName(YAMLValue value) {
        if (value instanceof YAMLMapping) {
            return Optional.ofNullable(((YAMLMapping) value).getKeyValueByKey("name"))
                    .map(YAMLKeyValue::getValue)
                    .map(PsiElement::getText)
                    .orElse(null);
        } else if (value instanceof YAMLPlainTextImpl) {
            final Matcher matcher = SHORTHAND.matcher(value.getText());
            final boolean b = matcher.find();
            return b ? matcher.group(1) : null;
        }
        return null;
    }

    @Override
    public TextRange getNameTextRange(YAMLValue value) {
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        if (matcher.find() && matcher.group(1) != null) {
            return new TextRange(matcher.start(1), matcher.end(1));
        }
        return TextRange.EMPTY_RANGE;
    }

    public @Nullable TextRange getTypePrefixRange(YAMLPlainTextImpl value) {
        final Matcher matcher = SHORTHAND_TYPED.matcher(value.getText());
        final boolean b = matcher.find();
        if (b && matcher.groupCount() == 4 && matcher.start(3) > -1) {
            return new TextRange(matcher.start(3), matcher.end(3));
        }
        return null;
    }
}
