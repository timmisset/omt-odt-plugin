package com.misset.opp.omt.psi.impl.model;

import com.misset.opp.omt.psi.model.OMTModelBlock;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.wrapper.OMTModelItemFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTModelBlockImpl extends YAMLKeyValueImpl implements OMTModelBlock  {
    public OMTModelBlockImpl(@NotNull YAMLKeyValue value) {
        super(value.getNode());
    }

    @Override
    public List<OMTModelItem> getModelItems() {
        return Optional.ofNullable(getValue())
                // obtain the mapping part from the model
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .map(YAMLMapping::getKeyValues)
                .orElse(Collections.emptyList())

                // stream all entries and map to specific ModelItems (Activity, Component etc)
                .stream()
                .map(OMTModelItemFactory::fromKeyValue)
                .collect(Collectors.toList());
    }
}
