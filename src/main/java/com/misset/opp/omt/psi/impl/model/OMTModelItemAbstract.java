package com.misset.opp.omt.psi.impl.model;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.wrapper.OMTElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class OMTModelItemAbstract extends YAMLBlockMappingImpl implements OMTModelItem {

    public OMTModelItemAbstract(@NotNull ASTNode node) {
        super(node);
    }

    protected List<OMTVariable> getVariables() {
        return getVariablesFrom("variables", OMTElementFactory::getVariable);
    }

    protected List<OMTVariable> getParams() {
        return getVariablesFrom("params", OMTElementFactory::getVariable);
    }

    private List<OMTVariable> getVariablesFrom(String key, Function<YAMLSequenceItem, OMTVariable> parser) {
        return Optional.ofNullable(getKeyValueByKey(key))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(YAMLSequence::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(parser)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
