package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTLocalVariableTypeProviderMetaType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.references.OMTPayloadQueryReferenceMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTOnChangeScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@ODTSimpleInjectable
public class OMTPayloadItemMetaType extends OMTLocalVariableTypeProviderMetaType implements ODTInjectable, OMTLocalVariableTypeProvider {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("value", ODTQueryMetaType::new);
        features.put("query", OMTPayloadQueryReferenceMetaType::new);
        features.put("list", YamlBooleanType::getSharedInstance);
        features.put("onChange", ODTOnChangeScriptMetaType::new);
    }

    public OMTPayloadItemMetaType() {
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
    protected YAMLValue getTypeProviderMap(String variableName,
                                           @NotNull YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("value"))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

    @Override
    protected List<String> getVariables() {
        return List.of("$newValue", "$oldValue");
    }
}
