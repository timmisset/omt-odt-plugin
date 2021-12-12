package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.misset.opp.callable.local.LocalCommand;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;

public interface OMTLocalCommandProvider extends OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLPsiElement, OMTLocalCommandProvider>>> KEY = new Key<>("OMTCallableProvider");

    HashMap<String, LocalCommand> getLocalCommandsMap();

    String getType();
}
