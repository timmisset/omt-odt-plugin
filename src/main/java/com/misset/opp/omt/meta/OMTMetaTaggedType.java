package com.misset.opp.omt.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class OMTMetaTaggedType extends OMTMetaType {

    private String name;
    protected OMTMetaTaggedType(@NotNull String name) {
        super(name);
        this.name = name;
    }

    protected abstract HashMap<String, Supplier<YamlMetaType>> getTaggedTypes();

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        if(existingMapping.getTag() == null) { return Collections.emptyList(); }
        final String tag = existingMapping
                .getTag()
                .getText();

        if(!isValidTag(tag)) { return Collections.emptyList(); }
        // delegate to the specific types:
        return getDelegateByTag(tag).computeKeyCompletions(existingMapping);
    }

    public boolean isValidTag(String tag) {
        return getTaggedTypes().containsKey(tag);
    }

    public YamlMetaType getDelegateByTag(String tag) {
        return getTaggedTypes().getOrDefault(tag, () -> null).get();
    }

    public Field getByTag(String tag) {
        return Optional.of(getDelegateByTag(tag))
                .map(yamlMetaType -> new Field(name, yamlMetaType))
                .orElse(null);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return new HashMap<>();
    }
}
