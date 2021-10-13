package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.TaggedElementContainer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.List;
import java.util.Set;

public class OMTModelItemType extends YamlMetaType implements TaggedElementContainer {

    private static final List<String> TAGS = List.of("!Activity", "!Component", "!Procedure", "!StandaloneQuery");

    private String name;

    public OMTModelItemType(@NonNls @NotNull String name) {
        super(name);
        this.name = name;
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

    @Override
    public boolean isValidTag(String tag) {
        return TAGS.contains(tag);
    }

    @Override
    public Field getByTag(String tag) {
        if("!Activity".equals(tag)) {
            return new Field(name, new OMTActivityType());
        }
        return null;
    }
}
