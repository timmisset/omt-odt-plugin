package com.misset.opp.omt.meta.providers;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

public interface OMTCallableProvider {
    @NotNull
    HashMap<String, List<PsiElement>> getCallableMap(YAMLMapping yamlMapping);
}
