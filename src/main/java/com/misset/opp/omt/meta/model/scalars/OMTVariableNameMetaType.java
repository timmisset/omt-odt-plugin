package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTVariableNameMetaType extends YamlScalarType implements OMTNamedVariableMetaType {
    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)");
    protected static final String SYNTAX_ERROR = "Invalid syntax for variable name, use: '$name'";

    public OMTVariableNameMetaType() {
        super("OMTVariableNameMetaType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        final Matcher matcher = SHORTHAND.matcher(scalarValue.getText());
        if (!matcher.find()) {
            holder.registerProblem(scalarValue, SYNTAX_ERROR);
        }
    }

    @Override
    public String getName(YAMLValue value) {
        final Matcher matcher = SHORTHAND.matcher(value.getText());
        final boolean b = matcher.find();
        return b ? matcher.group() : value.getText();
    }

    @Override
    public TextRange getNameTextRange(YAMLValue value) {
        return TextRange.allOf(value.getText());
    }

    @Override
    public Set<OntResource> getType(YAMLValue value) {
        // only the param: variable declaration has a typing, resolve via destructed meta-type
        final YAMLMapping yamlMapping = PsiTreeUtil.getParentOfType(value, YAMLMapping.class);
        return Optional.ofNullable(yamlMapping)
                .map(OMTMetaTypeProvider.getInstance(value.getProject())::getValueMetaType)
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTParamMetaType.class::isInstance)
                .map(OMTParamMetaType.class::cast)
                .map(omtParamMetaType -> omtParamMetaType.getTypeFromDestructed(yamlMapping))
                .orElse(Collections.emptySet());
    }
}
