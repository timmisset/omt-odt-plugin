package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.references.OMTPayloadQueryReferenceMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTOnChangeScriptMetaType;
import com.misset.opp.resolvable.local.LocalVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.Supplier;

@SimpleInjectable
public class OMTPayloadItemMetaType extends OMTMetaType implements
        OMTMetaInjectable,
        OMTLocalVariableTypeProvider {
    private static final OMTPayloadItemMetaType INSTANCE = new OMTPayloadItemMetaType();

    public static OMTPayloadItemMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("value", OMTQueryMetaType::getInstance);
        features.put("query", OMTPayloadQueryReferenceMetaType::getInstance);
        features.put("list", YamlBooleanType::getSharedInstance);
        features.put("onChange", OMTOnChangeScriptMetaType::getInstance);
    }

    private OMTPayloadItemMetaType() {
        super("OMT PayloadItem");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull List<String> computeMissingFields(@NotNull Set<String> existingFields) {
        // either query or value is required
        if (existingFields.contains("query") || existingFields.contains("value")) {
            return Collections.emptyList();
        }
        return List.of("value");
    }

    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        if (keyValue.getValue() instanceof YAMLMapping) {
            final YAMLMapping mapping = (YAMLMapping) keyValue.getValue();
            if (mapping.getKeyValueByKey("query") != null && mapping.getKeyValueByKey("value") != null) {
                problemsHolder.registerProblem(keyValue, "Use either 'value' or 'query'", ProblemHighlightType.ERROR);
            }
            if (mapping.getKeyValueByKey("query") != null && mapping.getKeyValueByKey("onChange") != null) {
                problemsHolder.registerProblem(keyValue,
                        "Cannot use 'onChange' with payload query",
                        ProblemHighlightType.ERROR);
            }
        }
    }

    @Override
    public List<LocalVariable> getLocalVariables(YAMLMapping mapping) {
        Set<OntResource> type = getType(mapping);
        return List.of(
                new LocalVariable("$newValue", "New value for the payload item", type, OMTOnChangeScriptMetaType.ONCHANGE_VARIABLE),
                new LocalVariable("$oldValue", "Old value for the payload item", type, OMTOnChangeScriptMetaType.ONCHANGE_VARIABLE)
        );
    }

    public YAMLValue getTypeProviderMap(@NotNull YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("value"))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

}
