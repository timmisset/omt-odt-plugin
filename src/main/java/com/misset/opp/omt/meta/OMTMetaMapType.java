package com.misset.opp.omt.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * MetaMap that provides an entry/item for any name that is entered as key
 * it has no opinion about the validity of keys
 */
public abstract class OMTMetaMapType extends OMTMetaType {

    protected OMTMetaMapType(@NotNull String name) {
        super(name);
    }

    protected abstract YamlMetaType getMapEntryType(String name);

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        return new Field(name, getMapEntryType(name));
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return new HashMap<>();
    }
}
