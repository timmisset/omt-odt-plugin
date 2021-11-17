package com.misset.opp.omt.psi.impl;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTProcedureMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class OMTCallable implements Callable {
    private final YAMLMapping mapping;

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

    private String getType() {
        return Optional.ofNullable(mapping.getParent())
                .filter(YAMLKeyValue.class::isInstance)
                .map(YAMLKeyValue.class::cast)
                .map(YAMLKeyValue::getValue)
                .map(YAMLValue::getTag)
                .map(element -> element.getText().substring(1))
                .orElse(null);
    }

    @Override
    public String getDescription(String context) {
        return getName() + " (" + getType() + ")";
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

    private OMTMetaType getMetaType() {
        return (OMTMetaType) Optional.ofNullable(OMTMetaTypeProvider.getInstance(mapping.getProject())
                        .getMetaTypeProxy(mapping))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    @Override
    public boolean isCommand() {
        return computeFromMeta(OMTMetaType.class,
                metaType -> metaType instanceof OMTActivityMetaType ||
                        metaType instanceof OMTProcedureMetaType,
                false);
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return Collections.emptyList();
    }

    @Override
    public String getCallId() {
        return (isCommand() ? "@" : "") + getName();
    }

    @Override
    public Set<OntResource> resolve(Set<OntResource> resources,
                                    Call call) {
        return computeFromMeta(com.misset.opp.omt.meta.OMTCallable.class,
                omtCallable -> omtCallable.resolve(mapping, resources, call),
                Collections.emptySet());
    }

    private <T, U> U computeFromMeta(Class<T> metaType,
                                     Function<T, U> ifMetaIsPresent,
                                     U orElse) {
        return Optional.ofNullable(getMetaType())
                .filter(omtMetaType -> metaType.isAssignableFrom(omtMetaType.getClass()))
                .map(metaType::cast)
                .map(ifMetaIsPresent)
                .orElse(orElse);
    }
}
