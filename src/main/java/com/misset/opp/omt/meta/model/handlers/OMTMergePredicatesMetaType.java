package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTPredicateQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTSubjectQueryType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersFromMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersUseMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergePredicatesMetaType extends OMTMetaType implements OMTDocumented {
    private static final Set<String> requiredFeatures = Set.of("subjects");
    protected static final String USE_IS_REQUIRED = "'use' is required when 'from' is 'both'";
    protected static final String USE_IS_ONLY_AVAILABLE = "'use' is only available when 'from' is 'both'";

    protected static final String ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED = "Either 'anyPredicate' or 'predicates' is required";
    protected static final String CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES = "Cannot combine 'anyPredicate' and 'predicates'";

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("subjects", OMTSubjectQueryType::new);
        features.put("anyPredicate", YamlBooleanType::getSharedInstance);
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
        if (value instanceof YAMLMapping) {
            YAMLMapping mapping = (YAMLMapping) value;
            // the merge-predicates has a specific combination of keys that are available based
            // on the values of siblings. These have to be analysed on the parent (handler) level
            validateUseWhenBoth(mapping, problemsHolder);
            validatePredicatesOrAnyPredicate(mapping, problemsHolder);
        }
    }

    private void validatePredicatesOrAnyPredicate(YAMLMapping mapping, ProblemsHolder problemsHolder) {
        boolean anyPredicate = mapping.getKeyValueByKey("anyPredicate") != null;
        boolean predicates = mapping.getKeyValueByKey("predicates") != null;
        if (!anyPredicate && !predicates) {
            problemsHolder.registerProblem(mapping, ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED);
        } else if (anyPredicate && predicates) {
            problemsHolder.registerProblem(mapping, CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES);
        }
    }

    private void validateUseWhenBoth(YAMLMapping mapping, ProblemsHolder problemsHolder) {
        if (isFromBoth(mapping) && !isUseSpecified(mapping)) {
            problemsHolder.registerProblem(mapping, USE_IS_REQUIRED);
        } else if (!isFromBoth(mapping) && isUseSpecified(mapping)) {
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

    @Override
    public String getDocumentationClass() {
        return "MergePredicates";
    }
}
