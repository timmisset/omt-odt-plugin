package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTVariableNameType extends YamlScalarType {
    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)");
    protected static final String SYNTAX_ERROR = "Invalid syntax for variable name, use: '$name'";
    public OMTVariableNameType() {
        super("OMTVariableNameType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Matcher matcher = SHORTHAND.matcher(scalarValue.getText());
        if(!matcher.find()) {
            holder.registerProblem(scalarValue, SYNTAX_ERROR);
        }
    }
}
