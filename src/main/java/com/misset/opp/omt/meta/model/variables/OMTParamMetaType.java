package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@ODTInjectable
public class OMTParamMetaType extends OMTMetaShorthandType {

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("type", OMTParamTypeType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)\\s*(\\(([^)]+)\\))?$");
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

}
