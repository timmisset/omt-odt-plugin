package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.model.scalars.OMTRuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;


public class OMTRulesType extends YamlMetaType {

    public OMTRulesType() {
        super("OMTRules");
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        return new Field(name, new OMTRuleType());
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        return Collections.emptyList();
    }

    @Override
    public void buildInsertionSuffixMarkup(@NotNull YamlInsertionMarkup markup,
                                           Field.@NotNull Relation relation,
                                           ForcedCompletionPath.@NotNull Iteration iteration) {

    }
}
