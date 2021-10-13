package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.actions.OMTActionsType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPayloadType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.OMTQueryWatcherType;
import com.misset.opp.omt.meta.model.OMTRulesType;
import com.misset.opp.omt.meta.model.handlers.OMTHandlerType;
import com.misset.opp.omt.meta.model.scalars.OMTCommandsType;
import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTODTScriptType;
import com.misset.opp.omt.meta.model.scalars.OMTQueriesType;
import com.misset.opp.omt.meta.model.scalars.OMTReasonType;
import com.misset.opp.omt.meta.model.variables.OMTParamType;
import com.misset.opp.omt.meta.model.variables.OMTVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class OMTActivityType extends YamlMetaType {
    protected OMTActivityType() {
        super("Activity");
    }

    @Override
    public @Nullable Field findFeatureByName(@NotNull String name) {
        YamlMetaType metaType = null;
        if ("title".equals(name)) {
            metaType = new YamlStringType();
        } else if ("onDefaultClose".equals(name)) {
            metaType = new YamlStringType();
        } else if ("params".equals(name)) {
            metaType = new YamlArrayType(new OMTParamType());
        } else if ("variables".equals(name)) {
            metaType = new YamlArrayType(new OMTVariableType());
        } else if ("handlers".equals(name)) {
            metaType = new YamlArrayType(new OMTHandlerType());
        } else if ("graphs".equals(name)) {
            metaType = new OMTGraphSelectionType();
        } else if ("watchers".equals(name)) {
            metaType = new YamlArrayType(new OMTQueryWatcherType());
        } else if ("payload".equals(name)) {
            metaType = new OMTPayloadType();
        } else if ("rules".equals(name)) {
            metaType = new OMTRulesType();
        } else if ("prefixes".equals(name)) {
            metaType = new OMTPrefixesType();
        } else if ("queries".equals(name)) {
            metaType = new OMTQueriesType();
        } else if ("commands".equals(name)) {
            metaType = new OMTCommandsType();
        } else if ("onStart".equals(name)) {
            metaType = new OMTODTScriptType();
        } else if ("onCommit".equals(name)) {
            metaType = new OMTODTScriptType();
        } else if ("onCancel".equals(name)) {
            metaType = new OMTODTScriptType();
        } else if ("onDone".equals(name)) {
            metaType = new OMTODTScriptType();
        } else if ("returns".equals(name)) {
            metaType = new OMTODTQueryType();
        } else if ("actions".equals(name)) {
            metaType = new OMTActionsType();
        } else if ("reason".equals(name)) {
            metaType = new OMTReasonType();
        }

        if (metaType != null) {
            return new Field(name, metaType);
        }
        return null;
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return Collections.emptyList();
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
