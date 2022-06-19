package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTActionsMapMetaType extends OMTMetaMapType implements OMTDocumented {

    private static final OMTActionsMapMetaType INSTANCE = new OMTActionsMapMetaType();

    public static OMTActionsMapMetaType getInstance() {
        return INSTANCE;
    }

    private OMTActionsMapMetaType() {
        super("Actions (map)");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTActionMetaType.getInstance();
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        super.validateValue(value, problemsHolder);
    }


    @Override
    public String getDocumentationClass() {
        return "Action";
    }
}
