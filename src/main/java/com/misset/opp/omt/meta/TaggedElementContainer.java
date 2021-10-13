package com.misset.opp.omt.meta;

import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.Field;

/**
 * Any container that can hold children with specific tags
 */
public interface TaggedElementContainer {

    boolean isValidTag(String tag);

    Field getByTag(String tag);
}
