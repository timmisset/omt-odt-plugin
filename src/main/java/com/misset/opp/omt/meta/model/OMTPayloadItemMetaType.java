package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableTypeProviderUtil;
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

    private static final String VALUE = "value";

    private static final String QUERY = "query";

    static {
        features.put(VALUE, OMTQueryMetaType::getInstance);
        features.put(QUERY, OMTPayloadQueryReferenceMetaType::getInstance);
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
        if (existingFields.contains(QUERY) || existingFields.contains(VALUE)) {
            return Collections.emptyList();
        }
        return List.of(VALUE);
    }

    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        if (keyValue.getValue() instanceof YAMLMapping) {
            final YAMLMapping mapping = (YAMLMapping) keyValue.getValue();
            if (mapping.getKeyValueByKey(QUERY) != null && mapping.getKeyValueByKey(VALUE) != null) {
                problemsHolder.registerProblem(keyValue, "Use either 'value' or 'query'", ProblemHighlightType.ERROR);
            }
            if (mapping.getKeyValueByKey(QUERY) != null && mapping.getKeyValueByKey("onChange") != null) {
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
        return Optional.ofNullable(mapping.getKeyValueByKey(VALUE))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

    @Override
    public Set<OntResource> getType(YAMLMapping mapping) {
        return OMTVariableTypeProviderUtil.getType(this, mapping);
    }

}
