package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;

public class ODTOperatorCompletion extends ODTCallCompletion {
    ElementPattern<PsiElement> BUILTIN_OPERATOR_STRICT =
            or(AFTER_FIRST_QUERY_STEP, INSIDE_DEFINED_QUERY, INSIDE_QUERY_FILTER);

    public ODTOperatorCompletion() {
        super(callable -> !callable.isCommand());

        extend(CompletionType.BASIC, QUERY_STEP, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                Predicate<Set<OntResource>> precedingFilter;

                ODTQueryStep queryStep = PsiTreeUtil.getParentOfType(position, ODTQueryStep.class, true);
                if (queryStep != null) {
                    addOperatorCompletions(parameters, result, position, typeFilter, queryStep);
                }
            }

            private void addOperatorCompletions(@NotNull CompletionParameters parameters,
                                                @NotNull CompletionResultSet result,
                                                PsiElement position,
                                                Predicate<Set<OntResource>> typeFilter,
                                                ODTQueryStep queryStep) {
                Predicate<Set<OntResource>> precedingFilter;
                Set<OntResource> previousStep = queryStep.resolvePreviousStep();
                precedingFilter = acceptableInput -> previousStep.isEmpty() ||
                        OppModel.INSTANCE.areCompatible(acceptableInput, previousStep);

                // add BuiltinOperators
                addBuiltinOperators(position, result, typeFilter, precedingFilter);

                // if inside a queries block, add the preceding sibling statements as callable options
                addCallables(getFromSiblingDefined(position), result, typeFilter, precedingFilter);

                // and all that are provided by the (if any) host file:
                addCallables(getFromCallableProviders((ODTFile) parameters.getOriginalFile()), result, typeFilter, precedingFilter);
            }

            private void addBuiltinOperators(PsiElement position,
                                             CompletionResultSet result,
                                             Predicate<Set<OntResource>> typeFilter,
                                             Predicate<Set<OntResource>> precedingFilter) {
                // static Operators are ones that don't require (actually don't want) to be applied on an input
                // but only as start. For example, the DATE_TIME stamp or IIF operator.
                if (BUILTIN_OPERATOR_STRICT.accepts(position)) {
                    addCallables(BuiltinOperators.getNonStaticOperators(), result, typeFilter, precedingFilter);
                    if (!AFTER_FIRST_QUERY_STEP.accepts(position)) {
                        addCallables(BuiltinOperators.getStaticOperators(), result, typeFilter, precedingFilter);
                    }
                } else {
                    // only add the static builtin operators:
                    addCallables(BuiltinOperators.getStaticOperators(), result, typeFilter, resources -> true);
                }
            }
        });

    }
}
