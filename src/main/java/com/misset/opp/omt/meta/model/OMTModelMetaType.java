package com.misset.opp.omt.meta.model;

import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addModelItemsToMap;

public class OMTModelMetaType extends OMTMetaMapType implements OMTCallableProvider {

    public OMTModelMetaType() {
        super("Model");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTModelItemMetaType(name);
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping, PsiLanguageInjectionHost host) {
        final HashMap<String, List<PsiCallable>> map = new HashMap<>();
        addModelItemsToMap(yamlMapping, map);
        return map;
    }
}
