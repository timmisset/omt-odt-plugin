package com.misset.opp.omt.psi.impl;

import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLSequenceItemImpl;

import java.util.Optional;

/**
 * A destructable notation of an otherwise wrapped (short-handed) value such as
 * a variable being destructed from:
 *      -   $variable (string)
 * to
 *      -   name: $variable
 *          type: string
 */
public abstract class DestructableYamlSequenceItem extends YAMLSequenceItemImpl {

    public DestructableYamlSequenceItem(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    protected YAMLPlainTextImpl getValueFor(String key) {
        if (isDestructed()) {
            return getValueByKey(key);
        } else {
            if (isPlainText()) {
                return (YAMLPlainTextImpl) getValue();
            }
        }
        return null;
    }

    @Nullable
    protected String getDestructedValue(String key) {
        if(!isDestructed()) { return null; }
        return Optional.ofNullable(getValueByKey(key))
                .map(ASTDelegatePsiElement::getText)
                .orElse(null);
    }

    protected boolean isDestructed() {
        return getValue() instanceof YAMLMapping;
    }

    protected boolean isPlainText() {
        return getValue() instanceof YAMLPlainTextImpl;
    }

    private YAMLPlainTextImpl getValueByKey(String key) {
        return Optional.ofNullable(getValue())
                .map(YAMLMapping.class::cast)
                .map(map -> map.getKeyValueByKey(key))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLPlainTextImpl.class::isInstance)
                .map(YAMLPlainTextImpl.class::cast)
                .orElse(null);
    }

}
