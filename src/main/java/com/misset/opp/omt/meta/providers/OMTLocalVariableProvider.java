package com.misset.opp.omt.meta.providers;

import com.misset.opp.resolvable.Variable;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Map;

public interface OMTLocalVariableProvider extends OMTMetaTypeStructureProvider {
    Map<String, Variable> getLocalVariableMap(YAMLPsiElement element);
}
