package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.yaml.psi.*;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class OMTVariableProviderUtil {

    private OMTVariableProviderUtil() {
        // empty constructor
    }

    public static String getVariableName(YAMLSequenceItem sequenceItem) {
        return getVariableName(sequenceItem.getValue(), "name");
    }

    public static String getVariableName(YAMLValue value, String destructedKeyId) {
        if (value instanceof YAMLPlainTextImpl) {
            return getVariableName((YAMLPlainTextImpl) value);
        } else if (value instanceof YAMLMapping) {
            return getVariableName((YAMLMapping) value, destructedKeyId);
        }
        return null;
    }

    private static String getVariableName(YAMLPlainTextImpl text) {
        return text.getText().split(" ")[0]; // remove any assignments or typings and only keep the $name
    }

    private static String getVariableName(YAMLMapping destructed, String destructedKeyId) {
        return Optional.ofNullable(destructed.getKeyValueByKey(destructedKeyId))
                .map(YAMLKeyValue::getValue)
                .map(PsiElement::getText)
                .orElse(null);
    }

    public static void addSequenceToMap(YAMLMapping mapping,
                                        String key,
                                        Map<String, Collection<PsiVariable>> map) {
        final YAMLKeyValue variables = mapping.getKeyValueByKey(key);
        if (variables != null && variables.getValue() instanceof YAMLSequence) {
            addSequenceToMap((YAMLSequence) variables.getValue(), map);
        }
    }

    public static void addSequenceToMap(YAMLSequence sequence,
                                        Map<String, Collection<PsiVariable>> map) {
        for (YAMLSequenceItem sequenceItem : sequence.getItems()) {
            String key = getVariableName(sequenceItem);
            OMTVariable referenceTarget = getReferenceTarget(sequenceItem);
            map.computeIfAbsent(key, s -> new ArrayList<>()).add(referenceTarget);
        }
    }

    private static OMTVariable getReferenceTarget(YAMLSequenceItem sequenceItem) {
        return getReferenceTarget(sequenceItem.getValue(), "name");
    }

    public static OMTVariable getReferenceTarget(YAMLValue yamlValue, String nameKeyIfMap) {
        if (yamlValue instanceof YAMLMapping) {
            // destructed notation, return the "name":
            final YAMLMapping yamlMapping = (YAMLMapping) yamlValue;
            final YAMLKeyValue name = yamlMapping.getKeyValueByKey(nameKeyIfMap);
            if (name == null || !(name.getValue() instanceof YAMLPlainTextImpl)) {
                return null;
            }
            yamlValue = name.getValue();
        }
        OMTYamlDelegate delegate = OMTYamlDelegateFactory.createDelegate(yamlValue);
        if (delegate instanceof OMTVariable) {
            return (OMTVariable) delegate;
        }
        return null;
    }

}
