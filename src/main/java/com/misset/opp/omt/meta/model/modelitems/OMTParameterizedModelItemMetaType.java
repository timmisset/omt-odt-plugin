package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class OMTParameterizedModelItemMetaType extends OMTModelItemDelegateMetaType implements OMTMetaCallable {
    private static final Key<CachedValue<HashMap<Integer, Set<OntResource>>>> PARAMETER_TYPES = new Key<>("PARAMETER_TYPES");
    private static final Logger LOGGER = Logger.getInstance(OMTParameterizedModelItemMetaType.class);

    protected OMTParameterizedModelItemMetaType(@NotNull String typeName) {
        super(typeName);
    }

    @Override
    public int minNumberOfArguments(YAMLMapping mapping) {
        return getInputParameters(mapping).size();
    }

    @Override
    public int maxNumberOfArguments(YAMLMapping mapping) {
        // input parameters are never optional, min and max number are identical
        return minNumberOfArguments(mapping);
    }

    private List<YAMLSequenceItem> getInputParameters(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("params"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(YAMLSequence::getItems)
                .orElse(Collections.emptyList());
    }

    @Override
    public Map<Integer, String> getParameterNames(YAMLMapping mapping) {
        List<YAMLSequenceItem> inputParameters = getInputParameters(mapping);
        HashMap<Integer, String> parameterNames = new HashMap<>();
        for (int i = 0; i < inputParameters.size(); i++) {
            String nameFromParameter = getNameFromParameter(inputParameters.get(i));
            if (nameFromParameter != null) {
                parameterNames.put(i, nameFromParameter);
            }
        }
        return parameterNames;
    }

    private String getNameFromParameter(YAMLSequenceItem sequenceItem) {
        YAMLValue value = sequenceItem.getValue();
        if (value == null) {
            return null;
        }
        YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = OMTMetaTypeProvider.getInstance(sequenceItem.getProject()).getMetaTypeProxy(sequenceItem);
        if (metaTypeProxy != null) {
            YamlMetaType metaType = metaTypeProxy.getMetaType();
            if (metaType instanceof OMTParamMetaType) {
                OMTParamMetaType paramMetaType = (OMTParamMetaType) metaType;
                return paramMetaType.getName(value);
            }
        }
        return null;
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes(YAMLMapping mapping) {
        return CachedValuesManager.getCachedValue(mapping,
                PARAMETER_TYPES,
                () -> new CachedValueProvider.Result<>(mapCallableParameters(calculateParameterTypes(mapping)), mapping.getContainingFile()));
    }

    private String getName(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getParent())
                .filter(YAMLKeyValue.class::isInstance)
                .map(YAMLKeyValue.class::cast)
                .map(YAMLKeyValue::getKeyText)
                .orElse(null);
    }

    private List<Set<OntResource>> calculateParameterTypes(YAMLMapping mapping) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "calculateParameterTypes for " + getName(mapping),
                () -> getInputParameters(mapping)
                        .stream()
                        .map(this::getTypeFromParameter)
                        .collect(Collectors.toList()));
    }

    private Set<OntResource> getTypeFromParameter(YAMLSequenceItem sequenceItem) {
        YAMLValue value = sequenceItem.getValue();
        if (value == null) {
            return Collections.emptySet();
        }
        YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = OMTMetaTypeProvider.getInstance(sequenceItem.getProject()).getMetaTypeProxy(sequenceItem);
        if (metaTypeProxy != null) {
            YamlMetaType metaType = metaTypeProxy.getMetaType();
            if (metaType instanceof OMTParamMetaType) {
                OMTParamMetaType paramMetaType = (OMTParamMetaType) metaType;
                return paramMetaType.getType(value);
            }
        }
        return Collections.emptySet();
    }

    private HashMap<Integer, Set<OntResource>> mapCallableParameters(List<Set<OntResource>> resources) {
        HashMap<Integer, Set<OntResource>> typeMapping = new HashMap<>();
        for (int i = 0; i < resources.size(); i++) {
            typeMapping.put(i, resources.get(i));
        }
        return typeMapping;
    }
}
