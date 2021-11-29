package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTOnChangeScriptMetaType;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SimpleInjectable
public class OMTVariableMetaType extends OMTMetaShorthandType implements OMTNamedVariableMetaType, OMTInjectable {

    private static final Set<String> requiredFeatures = Set.of("name");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("readonly", YamlBooleanType::getSharedInstance);
        features.put("value", OMTQueryMetaType::new);
        features.put("onChange", OMTOnChangeScriptMetaType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:=\\s*(.+))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name = someValue' OR '$name'";

    public OMTVariableMetaType() {
        super("OMT Variable");
    }

    @Override
    public List<TextRange> getTextRanges(YAMLScalarImpl host) {
        // only the value part of the assignment is injectable and to be resolved by the injected language
        final Matcher matcher = SHORTHAND.matcher(host.getText());
        if (matcher.find() && matcher.group(2) != null && !matcher.group(2).isBlank()) {
            final TextRange textRange = new TextRange(matcher.start(2), matcher.end(2));
            return Collections.singletonList(textRange);
        }
        return Collections.emptyList();
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
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
        if (value != null) {
            // if an injected Resolvable is present it will be resolved
            return resolveValue(value);
        }
        return Collections.emptySet();
    }

    @Override
    public TextRange getNameTextRange(YAMLValue value) {
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        if (matcher.find() && matcher.group(1) != null) {
            return new TextRange(matcher.start(1), matcher.end(1));
        }
        return TextRange.EMPTY_RANGE;
    }

    @Override
    public Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        final YAMLKeyValue keyValue = mapping.getKeyValueByKey("value");
        if (keyValue == null) {
            return Collections.emptySet();
        }
        return resolveValue(keyValue.getValue());
    }

    @Override
    public String getName(YAMLValue value) {
        return getNameTextRange(value).substring(value.getText());
    }
}
