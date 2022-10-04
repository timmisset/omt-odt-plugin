package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.List;
import java.util.Objects;

public class OMTHandlersArrayMetaType extends YamlArrayType {
    private static final OMTHandlersArrayMetaType INSTANCE = new OMTHandlersArrayMetaType();
    public static final String OVERWRITE_ALL_SHOULD_NOT_BE_COMBINED_WITH_OTHER_HANDLERS = "OverwriteAll should not be combined with other handlers";

    public static OMTHandlersArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTHandlersArrayMetaType() {
        super(OMTMergeHandlerMetaType.getInstance());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        List<YAMLSequenceItem> items = ((YAMLSequence) value).getItems();
        if (items.size() > 1 && items.stream()
                .map(YAMLSequenceItem::getValue)
                .filter(Objects::nonNull)
                .map(YAMLValue::getTag)
                .filter(Objects::nonNull)
                .anyMatch(element -> element.getText().equals("!OverwriteAll"))) {
            problemsHolder.registerProblem(value, OVERWRITE_ALL_SHOULD_NOT_BE_COMBINED_WITH_OTHER_HANDLERS, ProblemHighlightType.WARNING);
        }
    }

}
