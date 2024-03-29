package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTBindingParameterMetaType extends OMTMetaShorthandType implements OMTNamedVariableMetaType {

    private static final OMTBindingParameterMetaType INSTANCE = new OMTBindingParameterMetaType();

    public static OMTBindingParameterMetaType getInstance() {
        return INSTANCE;
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:\\(\\s*(sync|input|output)\\s*\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (sync|input|output)' OR $name'";

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("bindTo", OMTVariableNameMetaType::getInstance);
        features.put("input", YamlBooleanType::getSharedInstance);
        features.put("output", YamlBooleanType::getSharedInstance);
        features.put("onChange", OMTScriptMetaType::getInstance);
    }

    private OMTBindingParameterMetaType() {
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

    @Override
    public String getName(YAMLValue value) {
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        final boolean b = matcher.find();
        return b ? matcher.group(1) : value.getText();
    }

    @Override
    public TextRange getNameTextRange(YAMLValue value) {
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        if (matcher.find() && matcher.group(1) != null) {
            return new TextRange(matcher.start(1), matcher.end(1));
        }
        return TextRange.EMPTY_RANGE;
    }

}
