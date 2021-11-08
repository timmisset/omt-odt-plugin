package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaShorthandType;
import com.misset.opp.omt.meta.OMTTypeResolver;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTOnChangeScriptMetaType;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OMTVariableMetaType extends OMTMetaShorthandType implements OMTTypeResolver, ODTInjectable {

    private static final Set<String> requiredFeatures = Set.of("name");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("name", OMTVariableNameMetaType::new);
        features.put("readonly", YamlBooleanType::getSharedInstance);
        features.put("value", ODTQueryMetaType::new);
        features.put("onChange", ODTOnChangeScriptMetaType::new);
    }

    private static final Pattern SHORTHAND = Pattern.compile("^\\s*(\\$\\w+)\\s*(?:=\\s*(.+))?$");
    protected static final String SYNTAX_ERROR = "Invalid syntax for parameter shorthand, use: '$name = someValue' OR '$name'";

    public OMTVariableMetaType() {
        super("OMT Variable");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
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
        final Collection<ODTVariableAssignment> variableAssignments = OMTProviderUtil.getInjectedContent((YAMLValue) yamlPsiElement,
                ODTVariableAssignment.class);
        if (variableAssignments.isEmpty()) {
            return Collections.emptySet();
        } else {
            return variableAssignments.stream().findFirst()
                    .map(ODTVariableAssignment::getVariableValue)
                    .map(ODTVariableValue::getQuery)
                    .map(ODTResolvable::resolve)
                    .orElse(Collections.emptySet());
        }
    }

    @Override
    public Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        final YAMLKeyValue keyValue = mapping.getKeyValueByKey("value");
        if(keyValue == null) { return Collections.emptySet(); }
        final Collection<ODTQuery> queries = OMTProviderUtil.getInjectedContent(keyValue.getValue(),
                ODTQuery.class);
        if (queries.isEmpty()) {
            return Collections.emptySet();
        } else {
            return queries.stream().findFirst()
                    .map(ODTResolvable::resolve)
                    .orElse(Collections.emptySet());
        }
    }
}
