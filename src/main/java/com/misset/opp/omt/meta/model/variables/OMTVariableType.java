package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTOnChangeScriptType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OMTVariableType extends OMTMetaShorthandType {

    private static final Set<String> requiredFeatures = Set.of("name");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("name", OMTVariableType::new);
        features.put("readonly", () -> new YamlBooleanType("readonly"));
        features.put("value", OMTODTQueryType::new);
        features.put("onChange", OMTOnChangeScriptType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:=\\s*(.+))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name = someValue' OR '$name'";

    public OMTVariableType() {
        super("OMT Variable");
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
}
