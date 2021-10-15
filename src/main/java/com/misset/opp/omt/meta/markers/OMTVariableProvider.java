package com.misset.opp.omt.meta.markers;

import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.misset.opp.util.Collection.addToGroupedMap;

public interface OMTVariableProvider {
    HashMap<String, List<YAMLPsiElement>> getVariableMap(YAMLMapping yamlMapping);

    default String getVariableName(YAMLSequenceItem sequenceItem) {
        return getVariableName(sequenceItem.getValue(), "name");
    }
    default String getVariableName(YAMLValue value, String destructedKeyId) {
        if(value instanceof YAMLPlainTextImpl) {
            return getVariableName((YAMLPlainTextImpl) value);
        } else if (value instanceof YAMLMapping) {
            return getVariableName((YAMLMapping) value, destructedKeyId);
        }
        return null;
    }
    private String getVariableName(YAMLPlainTextImpl text) {
        return text.getText().split(" ")[0]; // remove any assignments or typings and only keep the $name
    }
    private String getVariableName(YAMLMapping destructed, String destructedKeyId) {
        return Optional.ofNullable(destructed.getKeyValueByKey(destructedKeyId))
                .map(YAMLKeyValue::getValue)
                .map(PsiElement::getText)
                .orElse(null);
    }

    default void addSequenceToMap(YAMLMapping mapping, String key, HashMap<String, List<YAMLPsiElement>> map) {
        final YAMLKeyValue variables = mapping.getKeyValueByKey(key);
        if(variables != null && variables.getValue() instanceof YAMLSequence) {
            addSequenceToMap((YAMLSequence) variables.getValue(), map);
        }
    }
    default void addSequenceToMap(YAMLSequence sequence, HashMap<String, List<YAMLPsiElement>> map) {
        sequence.getItems()
                .forEach(sequenceItem -> addToGroupedMap(getVariableName(sequenceItem), sequenceItem, map));
    }
}
