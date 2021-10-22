package com.misset.opp.omt.meta.model;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

public class OMTModelType extends OMTMetaMapType implements OMTCallableProvider {

    public OMTModelType() {
        super("ModelItem");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTModelItemType(name);
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getCallableMap(YAMLMapping yamlMapping) {
        return new HashMap<>();
    }
}
