package com.misset.opp.omt.meta.model.handlers;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.List;
import java.util.Set;

public class OMTHandlerType extends YamlMetaType {
    protected OMTHandlerType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    public OMTHandlerType() {
        super("Handler");
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        return null;
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return null;
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        return null;
    }

    @Override
    public void buildInsertionSuffixMarkup(@NotNull YamlInsertionMarkup markup,
                                           Field.@NotNull Relation relation,
                                           ForcedCompletionPath.@NotNull Iteration iteration) {

    }
}
