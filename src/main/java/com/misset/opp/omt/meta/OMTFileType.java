package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTModelType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.List;
import java.util.Set;

/**
 * The OMTFileType is the OMT Root and only contains specifically labelled features
 */
public class OMTFileType extends YamlMetaType {
    protected OMTFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        if(name.equals("model")) {
            return new Field("model", new OMTModelType(name));
        }
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
