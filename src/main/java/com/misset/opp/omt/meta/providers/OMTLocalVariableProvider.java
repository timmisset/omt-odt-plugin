package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.misset.opp.callable.local.LocalVariable;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.LinkedHashMap;
import java.util.Map;

public interface OMTLocalVariableProvider extends OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLValue, OMTLocalVariableProvider>>> KEY = new Key<>("OMTCallableProvider");

    Map<String, LocalVariable> getLocalVariableMap(YAMLPsiElement element);
}
