package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

public abstract class OMTMergeMetaType extends OMTMetaType {

    protected static final String ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED = "Either 'anyPredicate' or 'predicates' is required";
    protected static final String CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES = "Cannot combine 'anyPredicate' and 'predicates'";

    protected OMTMergeMetaType(@NotNull String typeName) {
        super(typeName);
    }

    protected void validatePredicatesOrAnyPredicate(YAMLMapping mapping, ProblemsHolder problemsHolder) {
        boolean anyPredicate = mapping.getKeyValueByKey("anyPredicate") != null;
        boolean predicates = mapping.getKeyValueByKey("predicates") != null;
        if (!anyPredicate && !predicates) {
            problemsHolder.registerProblem(mapping, ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED);
        } else if (anyPredicate && predicates) {
            problemsHolder.registerProblem(mapping, CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES);
        }
    }
}
