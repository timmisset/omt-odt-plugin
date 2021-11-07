package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.OMTTypeResolver;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ODTInjectable
public class OMTParamMetaType extends OMTMetaShorthandType implements OMTTypeResolver {

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

    @Override
    public Set<OntResource> getType(YAMLPsiElement yamlPsiElement) {
        if (!(yamlPsiElement instanceof YAMLValue)) {
            return null;
        }
        final Matcher matcher = SHORTHAND.matcher(yamlPsiElement.getText());
        final boolean b = matcher.find();
        if (!b || matcher.group(3) == null) {
            return null;
        } // no type provided
        return getType((YAMLValue) yamlPsiElement, matcher.group(3));
    }

    @Override
    public Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("type"))
                .map(YAMLKeyValue::getValue)
                .map(value -> getType(value, value.getText()))
                .orElse(Collections.emptySet());
    }

    private static Set<OntResource> getType(YAMLValue yamlValue,
                                            String value) {
        final OppModel oppModel = OppModel.INSTANCE;
        final Collection<ODTResolvableQualifiedUriStep> resolvableUriSteps = OMTProviderUtil.getInjectedContent(yamlValue,
                ODTResolvableQualifiedUriStep.class);
        if (resolvableUriSteps.isEmpty()) {
            // no curie, probably a primitive, try to parse:
            return Set.of(oppModel.parsePrimitive(value));
        } else {
            // curie is available, resolve to type:
            final String fullyQualifiedUri = resolvableUriSteps.stream().findFirst().get().getFullyQualifiedUri();
            return oppModel.getClass(fullyQualifiedUri)
                    .listInstances()
                    .mapWith(oppModel::getResource).toSet();
        }
    }
}