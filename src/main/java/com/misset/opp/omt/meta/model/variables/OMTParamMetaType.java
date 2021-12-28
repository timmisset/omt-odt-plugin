package com.misset.opp.omt.meta.model.variables;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.OMTOntologyTypeProvider;
import com.misset.opp.omt.meta.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.scalars.OMTVariableNameMetaType;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.misset.opp.omt.util.PatternUtil.getTextRange;

public class OMTParamMetaType extends OMTMetaShorthandType implements
        OMTOntologyTypeProvider,
        OMTNamedVariableMetaType {

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    /*
     * RegEx with capturing group that is able to extract the parameter name (group 1) and type (group 3)
     */
    private static final Pattern SHORTHAND = Pattern.compile("^(\\$\\w+)\\s*(\\(([^)]+)\\))?$");

    /*
     * RegEx with capturing group that is able to extract the parameter name (group 1) and prefix:localname (group 3 and 4)
     */
    public static final Pattern SHORTHAND_PREFIX_TYPED = Pattern.compile("^(\\$\\w+)\\s*(\\(([A-z0-9]+):([A-z0-9]+)\\))?$");

    /*
     * RegEx with capturing group that is able to extract the parameter name (group 1) and the qualified uri (group 2)
     */
    public static final Pattern SHORTHAND_URI_TYPED = Pattern.compile("^(\\$\\w+)\\s*\\((<\\S+>)\\)$");


    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name (type)' OR '$name'";

    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("type", OMTParamTypeType::new);
    }


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

    @Override
    public Set<OntResource> getType(YAMLValue value) {
        if (value instanceof YAMLMapping) {
            return getTypeFromDestructed((YAMLMapping) value);
        }
        String text = value.getText();
        TextRange textRange = getTextRange(text, SHORTHAND_URI_TYPED, 2)
                .or(() -> getTextRange(text, SHORTHAND, 3))
                .orElse(TextRange.EMPTY_RANGE);
        return OMTParamTypeType.resolveType(value, textRange.substring(text));
    }

    @Override
    public Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("type"))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTParamTypeType.resolveType(value, value.getText()))
                .orElse(Collections.emptySet());
    }

    @Override
    public String getName(YAMLValue value) {
        if (value instanceof YAMLMapping) {
            return Optional.ofNullable(((YAMLMapping) value).getKeyValueByKey("name"))
                    .map(YAMLKeyValue::getValue)
                    .map(PsiElement::getText)
                    .orElse(null);
        } else if (value instanceof YAMLPlainTextImpl) {
            return getTextRange(value.getText(), SHORTHAND, 1)
                    .orElse(TextRange.EMPTY_RANGE)
                    .substring(value.getText());
        }
        return null;
    }

    @Override
    public TextRange getNameTextRange(YAMLValue value) {
        return getTextRange(value.getText(), SHORTHAND, 1)
                .orElse(TextRange.EMPTY_RANGE);
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        super.validateValue(value, problemsHolder);
        OMTParamTypeType.validatePrefixReference(value, problemsHolder);
    }

    @Override
    public String getFullyQualifiedURI(YAMLPlainTextImpl value) {
        return getType(value)
                .stream()
                .map(OppModel.INSTANCE::toClass)
                .distinct()
                .map(Resource::getURI)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public TextRange getOntologyTypeTextRange(YAMLPlainTextImpl value) {
        String text = value.getText();
        return getTextRange(text, SHORTHAND_PREFIX_TYPED, 4)
                .or(() -> getTextRange(text, SHORTHAND_URI_TYPED, 2))
                .orElse(TextRange.EMPTY_RANGE);
    }


}
