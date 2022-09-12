package com.misset.opp.omt.meta;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.resolvable.psi.PsiResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class OMTMetaType extends YamlMetaType {

    protected OMTMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected abstract HashMap<String, Supplier<YamlMetaType>> getFeatures();

    /**
     * Override this method in child classes to enable missing field inspection
     */
    protected Set<String> getRequiredFields() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return getRequiredFields().stream()
                .filter(s -> !existingFields.contains(s))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        final HashMap<String, Supplier<YamlMetaType>> features = getFeatures();
        if (features == null) {
            return Collections.emptyList();
        }
        return features.keySet()
                .stream()
                .map(this::findFeatureByName)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        final YamlMetaType metaType = Optional.ofNullable(getFeatures())
                .map(map -> map.get(name))
                .map(Supplier::get)
                .orElse(null);
        if (metaType == null) {
            return null;
        }
        return new Field(name, metaType);
    }

    @Override
    public void buildInsertionSuffixMarkup(@NotNull YamlInsertionMarkup markup,
                                           Field.@NotNull Relation relation,
                                           ForcedCompletionPath.@NotNull Iteration iteration) {
    }

    public Set<OntResource> resolveValue(YAMLValue value) {
        final Collection<ODTQuery> injectedContent = OMTODTInjectionUtil.getInjectedContent(value, ODTQuery.class);
        if (injectedContent.isEmpty()) {
            return Collections.emptySet();
        } else {
            return injectedContent.stream().findFirst()
                    .map(PsiResolvable::resolve)
                    .orElse(Collections.emptySet());
        }
    }
}
