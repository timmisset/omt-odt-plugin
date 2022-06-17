package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
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

                ODTQueryStep queryStep = PsiTreeUtil.getParentOfType(position, ODTQueryStep.class, true);
                if (queryStep != null) {
                    addOperatorCompletions(parameters, result, context, position, typeFilter, queryStep);
                }
            }
        });
    }

    /**
     * Adds true/false and all available queries that return a boolean type
     */
    public void addBooleanCompletions(@NotNull CompletionParameters parameters,
                                      @NotNull CompletionResultSet result,
                                      @NotNull ProcessingContext context,
                                      PsiElement element) {
        result.addElement(PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create("true"), 100
        ));
        result.addElement(PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create("false"), 100
        ));

        Predicate<Set<OntResource>> filterType = resources -> OppModel.INSTANCE.areCompatible(Collections.singleton(OppModelConstants.XSD_BOOLEAN_INSTANCE), resources);
        ODTQueryStep queryStep = PsiTreeUtil.getParentOfType(element, ODTQueryStep.class);
        if (queryStep != null) {
            new ODTOperatorCompletion().addOperatorCompletions(parameters, result, context, element, filterType, queryStep);
        }
    }

    public void addOperatorCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull CompletionResultSet result,
                                       @NotNull ProcessingContext context,
                                       PsiElement position,
                                       Predicate<Set<OntResource>> typeFilter,
                                       ODTQueryStep queryStep) {
        Predicate<Set<OntResource>> precedingFilter;
        Set<OntResource> previousStep = queryStep.resolvePreviousStep();
        precedingFilter = acceptableInput -> previousStep.isEmpty() ||
                OppModel.INSTANCE.areCompatible(acceptableInput, previousStep);

        // add BuiltinOperators
        addBuiltinOperators(position, result, context, typeFilter, precedingFilter);

        // if inside a queries block, add the preceding sibling statements as callable options
        addCallables(getFromSiblingDefined(position), result, typeFilter, precedingFilter, context);

        // and all that are provided by the (if any) host file:
        addCallables(getFromCallableProviders((ODTFile) parameters.getOriginalFile()), result, typeFilter, precedingFilter, context);
    }

    private void addBuiltinOperators(PsiElement position,
                                     CompletionResultSet result,
                                     @NotNull ProcessingContext context,
                                     Predicate<Set<OntResource>> typeFilter,
                                     Predicate<Set<OntResource>> precedingFilter) {
        // static Operators are ones that don't require (actually don't want) to be applied on an input
        // but only as start. For example, the DATE_TIME stamp or IIF operator.
        if (BUILTIN_OPERATOR_STRICT.accepts(position)) {
            addCallables(BuiltinOperators.getNonStaticOperators(), result, typeFilter, precedingFilter, context);
            if (!AFTER_FIRST_QUERY_STEP.accepts(position)) {
                addCallables(BuiltinOperators.getStaticOperators(), result, typeFilter, precedingFilter, context);
            }
        } else {
            // only add the static builtin operators:
            addCallables(BuiltinOperators.getStaticOperators(), result, typeFilter, resources -> true, context);
        }
    }
}
