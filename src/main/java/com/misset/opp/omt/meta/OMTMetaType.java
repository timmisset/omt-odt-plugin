package com.misset.opp.omt.meta;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
        return getFeatures().keySet()
                .stream()
                .map(this::findFeatureByName)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        final YamlMetaType metaType = getFeatures()
                .get(name)
                .get();
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
}
