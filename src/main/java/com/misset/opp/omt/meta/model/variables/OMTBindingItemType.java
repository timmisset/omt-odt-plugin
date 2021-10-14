package com.misset.opp.omt.meta.model.variables;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTODTScriptType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTBindingItemType extends OMTMetaType {
    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:\\(\\s*(sync|input|output)\\s*\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (sync|input|output)' OR $name'";

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("bindTo", OMTVariableName::new);
        features.put("input", () -> new YamlBooleanType("input"));
        features.put("output", () -> new YamlBooleanType("output"));
        features.put("onChange", OMTODTScriptType::new);
    }

    public OMTBindingItemType() {
        super("OMT Bindings");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {

        // do not validate the destructed notation
        if(value instanceof YAMLMapping) { return; }

        // When the parameter is used as short-hand notation, the value itself is tested here
        // make sure it conforms to the RegEx used to determine the name and type:
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        if(!matcher.find()) {
            problemsHolder.registerProblem(value, SYNTAX_ERROR);
        }
    }
}
