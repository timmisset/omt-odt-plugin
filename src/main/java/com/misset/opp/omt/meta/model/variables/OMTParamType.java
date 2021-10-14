package com.misset.opp.omt.meta.model.variables;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTParamType extends YamlMetaType {
    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)\\s*(\\(([^)]+)\\))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (type)' OR $name'";

    public OMTParamType() {
        super("OMT Parameter");
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        if(name.equals("name")) {
            return new Field(name, new OMTVariableName());
        } else if (name.equals("type")) {
            return new Field(name, new OMTParamTypeType());
        }
        return null;
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        return Collections.emptyList();
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

    @Override
    public void buildInsertionSuffixMarkup(@NotNull YamlInsertionMarkup markup,
                                           Field.@NotNull Relation relation,
                                           ForcedCompletionPath.@NotNull Iteration iteration) {

    }
}
