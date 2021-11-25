package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.psi.PsiVariable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTVariableProviderUtil extends OMTProviderUtil {

    public static String getVariableName(YAMLSequenceItem sequenceItem) {
        return getVariableName(sequenceItem.getValue(), "name");
    }
    public static String getVariableName(YAMLValue value, String destructedKeyId) {
        if(value instanceof YAMLPlainTextImpl) {
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

    public static void addSequenceToMap(YAMLMapping mapping, String key, HashMap<String, List<PsiElement>> map) {
        final YAMLKeyValue variables = mapping.getKeyValueByKey(key);
        if(variables != null && variables.getValue() instanceof YAMLSequence) {
            addSequenceToMap((YAMLSequence) variables.getValue(), map);
        }
    }

    public static void addSequenceToMap(YAMLSequence sequence,
                                        HashMap<String, List<PsiElement>> map) {
        sequence.getItems()
                .forEach(sequenceItem -> addToGroupedMap(getVariableName(sequenceItem),
                        getReferenceTarget(sequenceItem),
                        map));
    }

    public static PsiVariable getReferenceTarget(YAMLSequenceItem sequenceItem) {
        final YAMLValue yamlValue = sequenceItem.getValue();
        if (yamlValue instanceof YAMLMapping) {
            // destructed notation, return the "name":
            final YAMLMapping yamlMapping = (YAMLMapping) yamlValue;
            final YAMLKeyValue name = yamlMapping.getKeyValueByKey("name");
            if (name == null) {
                return null;
            }
            return getVariable(name.getValue());
        } else {
            return getVariable(yamlValue);
        }
    }

    public static PsiVariable getVariable(YAMLValue value) {
        return getInjectedContent(value, PsiVariable.class).stream().findFirst().orElse(null);
    }

}
