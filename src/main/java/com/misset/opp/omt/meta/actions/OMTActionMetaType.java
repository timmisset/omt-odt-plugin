package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTOnSelectScriptMetaType;
import com.misset.opp.resolvable.local.LocalVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.*;

import java.util.*;
import java.util.function.Supplier;

public class OMTActionMetaType extends OMTMetaType implements OMTVariableProvider, OMTDocumented, OMTLocalVariableTypeProvider {
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
        features.put("onSelect", OMTOnSelectScriptMetaType::new);
    }

    public OMTActionMetaType(boolean mapped) {
        super("Action");
        this.mapped = mapped;
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

    @Override
    public String getDocumentationClass() {
        return "Action";
    }

    public YAMLValue getTypeProviderMap(@NotNull YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("dynamicActionQuery"))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

    @Override
    public List<LocalVariable> getLocalVariables(YAMLMapping mapping) {
        if (mapping.getKeyValueByKey("dynamicActionQuery") == null) {
            return Collections.emptyList();
        }
        Set<OntResource> type = getType(mapping);
        return List.of(
                new LocalVariable("$value", "Individual result of the DynamicActionQuery", type)
        );
    }
}
