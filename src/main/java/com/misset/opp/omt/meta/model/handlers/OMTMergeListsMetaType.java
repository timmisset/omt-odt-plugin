package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTPredicateQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTSubjectQueryType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersFromMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeListsMetaType extends OMTMergeMetaType implements OMTDocumented {
    private static final Set<String> requiredFeatures = Set.of("subjects");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("subjects", OMTSubjectQueryType::new);
        features.put("anyPredicate", YamlBooleanType::getSharedInstance);
        features.put("predicates", OMTPredicateQueryType::new);
        features.put("when", OMTBooleanQueryType::new);
        features.put("from", OMTHandlersFromMetaType::new);
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        if (value instanceof YAMLMapping) {
            validatePredicatesOrAnyPredicate((YAMLMapping) value, problemsHolder);
        }
    }

    protected OMTMergeListsMetaType() {
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
    public String getDocumentationClass() {
        return "MergeLists";
    }
}
