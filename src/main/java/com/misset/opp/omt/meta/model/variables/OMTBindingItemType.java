package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableName;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OMTBindingItemType extends OMTMetaShorthandType {
    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:\\(\\s*(sync|input|output)\\s*\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (sync|input|output)' OR $name'";

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("bindTo", OMTVariableName::new);
        features.put("input", () -> new YamlBooleanType("input"));
        features.put("output", () -> new YamlBooleanType("output"));
        features.put("onChange", OMTScriptType::new);
    }

    public OMTBindingItemType() {
        super("OMT Bindings");
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
}
