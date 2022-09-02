package com.misset.opp.omt.meta.providers;

import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.HashMap;

public interface OMTVariableProvider {
    @NotNull
    HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping yamlMapping);
}
