package com.misset.opp.omt.meta.providers;

import com.misset.opp.callable.local.LocalVariable;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Map;

public interface OMTLocalVariableProvider {
    Map<String, LocalVariable> getLocalVariableMap(YAMLPsiElement element);
}
