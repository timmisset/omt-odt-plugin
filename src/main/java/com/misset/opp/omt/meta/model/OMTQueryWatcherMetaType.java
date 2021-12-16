package com.misset.opp.omt.meta.model;

import com.misset.opp.callable.Resolvable;
import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.callable.psi.PsiResolvable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTOnChangeScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.Supplier;

public class OMTQueryWatcherMetaType extends OMTMetaType implements
        OMTLocalVariableTypeProvider {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("query", OMTQueryMetaType::new);
        features.put("onChange", OMTOnChangeScriptMetaType::new);
    }

    public OMTQueryWatcherMetaType() {
        super("OMT QueryWatcher");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public List<LocalVariable> getLocalVariables(YAMLMapping mapping) {
        Set<OntResource> type = getType(mapping);
        return List.of(
                new LocalVariable("$newValue", "New value for the variable", type),
                new LocalVariable("$oldValue", "Old value for the variable", type)
        );
    }

    private Set<OntResource> getType(YAMLMapping mapping) {
        final YAMLValue yamlValue = getTypeProviderMap(mapping);
        return Optional.ofNullable(yamlValue)
                .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvable.class))
                .orElse(Collections.emptySet())
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }

    private YAMLValue getTypeProviderMap(@NotNull YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("query"))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }
}
