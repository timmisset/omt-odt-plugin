package com.misset.opp.omt.psi.impl;

import com.misset.opp.callable.Callable;
import com.misset.opp.callable.builtin.commands.LocalVariable;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTProcedureMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OMTCallable implements Callable {
    private YAMLMapping mapping;
    public OMTCallable(YAMLMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public String getName() {
        return Optional.ofNullable(mapping.getParent())
                .filter(YAMLKeyValue.class::isInstance)
                .map(YAMLKeyValue.class::cast)
                .map(YAMLKeyValue::getKeyText)
                .orElse(null);
    }

    @Override
    public String getDescription(String context) {
        return null;
    }

    @Override
    public int minNumberOfArguments() {
        return Optional.ofNullable(mapping.getKeyValueByKey("params"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(sequence -> sequence.getItems().size())
                .orElse(0);
    }

    @Override
    public int maxNumberOfArguments() {
        // input parameters are never optional, min and max number are identical
        return minNumberOfArguments();
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(mapping.getProject()).getMetaTypeProxy(mapping))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .map(metaType ->
                        metaType instanceof OMTActivityMetaType ||
                        metaType instanceof OMTProcedureMetaType)
                .orElse(false);
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return Collections.emptyList();
    }

    @Override
    public String getCallId() {
        return (isCommand() ? "@" : "") + getName();
    }
}
