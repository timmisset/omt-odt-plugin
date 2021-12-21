package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTPredicateQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTSubjectQueryType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersFromMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersUseMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergePredicatesMetaType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("subjects", "predicates");
    protected static final String USE_IS_REQUIRED = "'use' is required when 'from' is 'both'";
    protected static final String USE_IS_ONLY_AVAILABLE = "'use' is only available when 'from' is 'both'";

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("subjects", OMTSubjectQueryType::new);
        features.put("predicates", OMTPredicateQueryType::new);
        features.put("when", OMTBooleanQueryType::new);
        features.put("from", OMTHandlersFromMetaType::new);
        features.put("use", OMTHandlersUseMetaType::new);
        features.put("type", OMTHandlersMetaType::new);
    }


    protected OMTMergePredicatesMetaType() {
        super("OMT MergePredicates");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        if(value instanceof YAMLMapping) {
            // the merge-predicates has a specific combination of keys that are available based
            // on the values of siblings. These have to be analysed on the parent (handler) level
            validateUseWhenBoth((YAMLMapping) value, problemsHolder);
        }
    }

    private void validateUseWhenBoth(YAMLMapping mapping, ProblemsHolder problemsHolder) {
        if(isFromBoth(mapping) && !isUseSpecified(mapping)) {
            problemsHolder.registerProblem(mapping, USE_IS_REQUIRED);
        } else if(!isFromBoth(mapping) && isUseSpecified(mapping)) {
            problemsHolder.registerProblem(mapping, USE_IS_ONLY_AVAILABLE);
        }
    }
    private boolean isFromBoth(YAMLMapping mapping) {
        return Optional.ofNullable(mapping.getKeyValueByKey("from"))
                .map(YAMLKeyValue::getValueText)
                .map(s -> s.equals("both"))
                .orElse(false);
    }
    private boolean isUseSpecified(YAMLMapping mapping) {
        return mapping.getKeyValueByKey("use") != null;
    }
}
