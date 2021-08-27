package com.misset.opp.omt.psi.impl;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLSequenceItemImpl;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTVariableImpl extends YAMLSequenceItemImpl implements OMTVariable {
    private static final Pattern NAME_CAPTURE = Pattern.compile("\\$([^\\s]*)");

    public OMTVariableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getName() {
        return getName(getValueFor("name"));
    }

    @Nullable
    private YAMLPlainTextImpl getValueFor(String key) {
        if (isDestructed()) {
            return getValueByKey(key);
        } else {
            if (isPlainText()) {
                return (YAMLPlainTextImpl) getValue();
            }
        }
        return null;
    }

    private String getName(@Nullable YAMLPlainTextImpl plainText) {
        if (plainText == null) {
            return null;
        }
        Matcher matcher = NAME_CAPTURE.matcher(plainText.getTextValue());
        final boolean b = matcher.find();
        return b ? matcher.group(1) : null;
    }

    private boolean isDestructed() {
        return getValue() instanceof YAMLMapping;
    }

    private boolean isPlainText() {
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
