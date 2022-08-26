package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTOnChangeScriptMetaType;
import com.misset.opp.resolvable.local.LocalVariable;
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
public class OMTVariableMetaType extends OMTMetaShorthandType implements
        OMTNamedVariableMetaType,
        OMTLocalVariableTypeProvider,
        OMTMetaInjectable,
        OMTDocumented {
    private static final OMTVariableMetaType INSTANCE = new OMTVariableMetaType();

    public static OMTVariableMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> requiredFeatures = Set.of("name");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("name", OMTVariableNameMetaType::getInstance);
        features.put("readonly", YamlBooleanType::getSharedInstance);
        features.put("value", OMTQueryMetaType::getInstance);
        features.put("onChange", OMTOnChangeScriptMetaType::getInstance);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:=\\s*(.+))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name = someValue' OR '$name'";

    private OMTVariableMetaType() {
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
        if (value == null) {
            return Collections.emptySet();
        } else if (value instanceof YAMLMapping) {
            return getTypeFromDestructed((YAMLMapping) value);
        } else {
            return resolveValue(value);
        }
    }

    @Override
    public boolean isReadonly(YAMLValue value) {
        if (!(value instanceof YAMLMapping)) {
            return false;
        }
        YAMLMapping yamlMapping = (YAMLMapping) value;
        YAMLKeyValue readonly = yamlMapping.getKeyValueByKey("readonly");
        return readonly != null && "true".equals(readonly.getValueText());
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

    @Override
    public List<LocalVariable> getLocalVariables(YAMLMapping mapping) {
        Set<OntResource> type = getType(mapping);
        return List.of(
                new LocalVariable("$newValue", "New value for the variable", type, OMTOnChangeScriptMetaType.ONCHANGE_VARIABLE),
                new LocalVariable("$oldValue", "Old value for the variable", type, OMTOnChangeScriptMetaType.ONCHANGE_VARIABLE)
        );
    }

    @Override
    public YAMLValue getTypeProviderMap(YAMLMapping mapping) {
        // not used
        return null;
    }

    @Override
    public String getDocumentationClass() {
        return "VariableDeclaration";
    }
}
