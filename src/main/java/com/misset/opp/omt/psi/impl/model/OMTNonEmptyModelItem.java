package com.misset.opp.omt.psi.impl.model;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.wrapper.OMTElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class OMTNonEmptyModelItem extends YAMLBlockMappingImpl implements OMTModelItem {

    public OMTNonEmptyModelItem(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public List<OMTVariable> getDeclaredVariables() {
        return Optional.ofNullable(getKeyValueByKey("variables"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(YAMLSequence::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(OMTElementFactory::getVariable)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
