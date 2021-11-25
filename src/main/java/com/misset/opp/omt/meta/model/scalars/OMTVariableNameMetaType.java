package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SimpleInjectable
public class OMTVariableNameMetaType extends YamlScalarType implements OMTInjectable {
    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)");
    protected static final String SYNTAX_ERROR = "Invalid syntax for variable name, use: '$name'";

    public OMTVariableNameMetaType() {
        super("OMTVariableNameMetaType");
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
