package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.*;

import java.util.*;
import java.util.function.Supplier;

public class OMTActionMetaType extends OMTMetaType implements OMTVariableProvider {
    private final boolean mapped;
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("id", YamlStringType::new);
        features.put("title", OMTInterpolatedStringMetaType::new);
        features.put("description", OMTInterpolatedStringMetaType::new);
        features.put("promoteSubMenuItemToMainMenu", OMTBooleanQueryType::new);
        features.put("icon", YamlStringType::new);
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("precondition", OMTQueryMetaType::new);
        features.put("disabled", OMTBooleanQueryType::new);
        features.put("busyDisabled", OMTBooleanQueryType::new);
        features.put("dynamicActionQuery", OMTQueryMetaType::new);
        features.put("onSelect", OMTScriptMetaType::new);
    }

    public OMTActionMetaType(boolean mapped) {
        super("Action");
        this.mapped = mapped;
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        return !mapped && !existingFields.contains("id") ? List.of("id") : Collections.emptyList();
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        OMTVariableProviderUtil.addSequenceToMap(mapping, "params", variableMap, true);

        return variableMap;
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        if (!(value instanceof YAMLMapping)) {
            return;
        }
        YAMLMapping mapping = (YAMLMapping) value;
        YAMLKeyValue id = mapping.getKeyValueByKey("id");
        if (id == null) {
            return;
        }

        if (mapped) {
            problemsHolder.registerProblem(id,
                    "Unnecessary id field when action is mapped to a key",
                    ProblemHighlightType.WARNING);
        } else {
            String idValue = id.getValueText();
            YAMLSequence sequence = PsiTreeUtil.getParentOfType(value, YAMLSequence.class);
            YAMLSequenceItem sequenceItem = PsiTreeUtil.getParentOfType(value, YAMLSequenceItem.class);

            if (sequence == null || sequenceItem == null) {
                return;
            }
            sequence.getItems().stream()
                    .filter(sibling -> sibling != sequenceItem)
                    .map(YAMLSequenceItem::getValue)
                    .filter(YAMLMapping.class::isInstance)
                    .map(YAMLMapping.class::cast)
                    .map(siblingMap -> siblingMap.getKeyValueByKey("id"))
                    .filter(Objects::nonNull)
                    .map(YAMLKeyValue::getValueText)
                    .filter(idValue::equals)
                    .findFirst()
                    .ifPresent(s -> problemsHolder.registerProblem(id, "Duplicate", ProblemHighlightType.ERROR));
        }
    }
}
