package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OMTParamType extends OMTMetaShorthandType {

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("name", OMTVariableNameType::new);
        features.put("type", OMTParamTypeType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)\\s*(\\(([^)]+)\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (type)' OR '$name'";

    public OMTParamType() {
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

}
