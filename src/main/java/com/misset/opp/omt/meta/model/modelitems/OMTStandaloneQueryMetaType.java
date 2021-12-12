package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiResolvable;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.*;

import java.util.*;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;
import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTStandaloneQueryMetaType extends OMTModelItemDelegateMetaType implements
        OMTVariableProvider,
        OMTPrefixProvider,
        OMTMetaCallable {
    protected OMTStandaloneQueryMetaType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("base", OMTVariableNameMetaType::new);
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("query", OMTQueryMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "params", variableMap, true);

        final YAMLKeyValue base = mapping.getKeyValueByKey("base");
        if (base != null) {
            // base should adhere to the OMTVariableNameMetaType otherwise it will throw an error on the syntax check
            YAMLValue value = base.getValue();
            OMTVariableProvider.IS_PARAMETER.set(value, true);
            addToGroupedMap(base.getValueText(), value, variableMap);
        }

        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping,
                                    Set<OntResource> resources,
                                    Call call) {

        // intercept the call and provide context for the parameters (if any)
        final YAMLKeyValue base = mapping.getKeyValueByKey("base");
        // the parameter entered at the base is populated with the leading resources of the call
        if (base != null) {
            call.setParamType(base.getValueText(), resources);
        }

        final YAMLKeyValue params = mapping.getKeyValueByKey("params");
        if (params != null && params.getValue() instanceof YAMLSequence) {
            final List<YAMLSequenceItem> items = ((YAMLSequence) params.getValue()).getItems();
            for (int i = 0; i < items.size(); i++) {
                final String variableName = new OMTParamMetaType().getName(items.get(i).getValue());
                call.setParamType(variableName, call.resolveSignatureArgument(i));
            }
        }

        return Optional.ofNullable(mapping.getKeyValueByKey("query"))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvable.class))
                .stream()
                .flatMap(Collection::stream)
                .map(psiResolvable -> psiResolvable.resolve(resources, call))
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
    public String getType() {
        return "StandaloneQuery";
    }
}
