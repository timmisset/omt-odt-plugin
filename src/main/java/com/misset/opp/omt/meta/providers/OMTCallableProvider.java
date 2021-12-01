package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface OMTCallableProvider extends OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLMapping, OMTCallableProvider>>> KEY = new Key<>("OMTCallableProvider");

    @NotNull
    HashMap<String, List<PsiElement>> getCallableMap(YAMLMapping yamlMapping);
}
