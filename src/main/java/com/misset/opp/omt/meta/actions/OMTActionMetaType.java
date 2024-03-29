package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.commands.LocalVariable;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import com.misset.opp.omt.meta.providers.util.OMTVariableTypeProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTActionScriptMetaType;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.*;

import java.util.*;
import java.util.function.Supplier;

public class OMTActionMetaType extends OMTMetaType implements OMTVariableProvider, OMTDocumented, OMTLocalVariableTypeProvider {
    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    protected static final String TITLE = "title";
    protected static final String DESCRIPTION = "description";
    private static final String DYNAMIC_ACTION_QUERY = "dynamicActionQuery";
    private static final String ID = "id";

    private static final OMTActionMetaType INSTANCE = new OMTActionMetaType();

    public static OMTActionMetaType getInstance() {
        return INSTANCE;
    }

    static {
        features.put(ID, YamlStringType::getInstance);
        features.put(TITLE, OMTInterpolatedStringMetaType::getInstance);
        features.put(DESCRIPTION, OMTInterpolatedStringMetaType::getInstance);
        features.put("promoteSubMenuItemToMainMenu", OMTBooleanQueryType::getInstance);
        features.put("icon", YamlStringType::getInstance);
        features.put("params", OMTParamsArrayMetaType::getInstance);
        features.put("precondition", OMTQueryMetaType::getInstance);
        features.put("disabled", OMTBooleanQueryType::getInstance);
        features.put("busyDisabled", OMTBooleanQueryType::getInstance);
        features.put(DYNAMIC_ACTION_QUERY, OMTQueryMetaType::getInstance);
        features.put("onSelect", OMTActionScriptMetaType::getInstance);
    }

    protected OMTActionMetaType() {
        super("Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, Collection<PsiVariable>> variableMap = new HashMap<>();
        OMTVariableProviderUtil.addSequenceToMap(mapping, "params", variableMap);

        return variableMap;
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        if (!(value instanceof YAMLMapping)) {
            return;
        }
        YAMLMapping mapping = (YAMLMapping) value;
        YAMLKeyValue id = mapping.getKeyValueByKey(ID);
        if (id == null) {
            return;
        }
        boolean mapped = value.getParent() instanceof YAMLKeyValue;
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
                    .map(siblingMap -> siblingMap.getKeyValueByKey(ID))
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

    @Override
    public YAMLValue getTypeProviderMap(@NotNull YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey(DYNAMIC_ACTION_QUERY))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

    @Override
    public Set<OntResource> getType(YAMLMapping mapping) {
        return OMTVariableTypeProviderUtil.getType(this, mapping);
    }

    @Override
    public List<LocalVariable> getLocalVariables(YAMLMapping mapping) {
        if (mapping.getKeyValueByKey(DYNAMIC_ACTION_QUERY) == null) {
            return Collections.emptyList();
        }
        Set<OntResource> type = getType(mapping);
        return List.of(
                new LocalVariable("$value", "Individual result of the DynamicActionQuery", type, "DynamicActionQuery variable")
        );
    }
}
