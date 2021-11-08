package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;
import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTStandaloneQueryMetaType extends OMTModelItemDelegateMetaType implements OMTVariableProvider, OMTPrefixProvider {
    protected OMTStandaloneQueryMetaType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("base", OMTVariableNameMetaType::new);
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("query", ODTQueryMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "params", variableMap);

        final YAMLKeyValue base = mapping.getKeyValueByKey("base");
        if(base != null) {
            // base should adhere to the OMTVariableNameMetaType otherwise it will throw an error on the syntax check
            addToGroupedMap(base.getValueText(), OMTVariableProviderUtil.getVariable(base.getValue()), variableMap);
        }

        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }
}
