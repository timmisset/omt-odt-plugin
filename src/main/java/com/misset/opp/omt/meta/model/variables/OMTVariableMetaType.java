package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTOnChangeScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OMTVariableMetaType extends OMTMetaShorthandType implements ODTInjectable {

    private static final Set<String> requiredFeatures = Set.of("name");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("readonly", YamlBooleanType::getSharedInstance);
        features.put("value", ODTQueryMetaType::new);
        features.put("onChange", ODTOnChangeScriptMetaType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:=\\s*(.+))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name = someValue' OR '$name'";

    public OMTVariableMetaType() {
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
