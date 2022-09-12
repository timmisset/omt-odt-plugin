package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTBaseParameterMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.*;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;

public class OMTStandaloneQueryMetaType extends OMTParameterizedModelItemMetaType implements
        OMTVariableProvider,
        OMTPrefixProvider,
        OMTMetaCallable,
        OMTDocumented {
    private OMTStandaloneQueryMetaType() {
        super("OMT StandaloneQuery");
    }

    private static final OMTStandaloneQueryMetaType INSTANCE = new OMTStandaloneQueryMetaType();

    public static OMTStandaloneQueryMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    private static final String PARAMS = "params";

    static {
        features.put("base", OMTBaseParameterMetaType::getInstance);
        features.put(PARAMS, OMTParamsArrayMetaType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put("query", OMTQueryMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, Collection<PsiVariable>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, PARAMS, variableMap);
        OMTVariable baseVariable = OMTVariableProviderUtil.getReferenceTarget(mapping, "base");
        if (baseVariable != null) {
            variableMap.computeIfAbsent(baseVariable.getName(), s -> new ArrayList<>()).add(baseVariable);
        }
        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {

        PsiCall call = context.getCall();
        Set<OntResource> resources = call.resolvePreviousStep();
        // intercept the call and provide context for the parameters (if any)
        final YAMLKeyValue base = mapping.getKeyValueByKey("base");
        // the parameter entered at the base is populated with the leading resources of the call
        if (base != null) {
            call.setParamType(base.getValueText(), resources);
        }

        final YAMLKeyValue params = mapping.getKeyValueByKey(PARAMS);
        if (params != null && params.getValue() instanceof YAMLSequence) {
            final List<YAMLSequenceItem> items = ((YAMLSequence) params.getValue()).getItems();
            for (int i = 0; i < items.size(); i++) {
                final String variableName = OMTParamMetaType.getInstance().getName(items.get(i).getValue());
                call.setParamType(variableName, call.resolveSignatureArgument(i));
            }
        }

        return Optional.ofNullable(mapping.getKeyValueByKey("query"))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTODTInjectionUtil.getInjectedContent(value, ODTQuery.class))
                .stream()
                .flatMap(Collection::stream)
                .map(psiResolvable -> psiResolvable.resolve(context))
                .findFirst()
                .orElse(Collections.emptySet());
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return false;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Collections.emptySet();
    }

    @Override
    public CallableType getType() {
        return CallableType.STANDALONE_QUERY;
    }

    @Override
    public String getDocumentationClass() {
        return "StandaloneQuery";
    }

    @Override
    public @NotNull Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping) {
        return OMTPrefixProviderUtil.getPrefixMap(yamlMapping);
    }

    @Override
    public @NotNull Map<String, String> getNamespaces(YAMLMapping yamlMapping) {
        return OMTPrefixProviderUtil.getNamespaces(yamlMapping);
    }
}
