package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;
import static com.misset.opp.omt.completion.OMTODTInjectableSectionCompletion.TYPE_FILTER;
import static com.misset.opp.omt.completion.OMTODTInjectableSectionCompletion.sharedContext;

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
                if (sharedContext != null && sharedContext.get(TYPE_FILTER) != null) {
                    typeFilter = typeFilter.and(sharedContext.get(TYPE_FILTER));
                }

                ODTQueryStep queryStep = PsiTreeUtil.getParentOfType(position, ODTQueryStep.class, true);
                if (queryStep != null) {
                    addOperatorCompletions(parameters, result, context, position, typeFilter, queryStep);
                }
            }
        });
    }

    public void addOperatorCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull CompletionResultSet result,
                                       @NotNull ProcessingContext context,
                                       PsiElement position,
                                       Predicate<Set<OntResource>> typeFilter,
                                       ODTQueryStep queryStep) {
        PsiFile originalFile = parameters.getOriginalFile();
        if (!(originalFile instanceof ODTFile)) {
            return;
        }

        ODTFile file = (ODTFile) originalFile;

        Predicate<Set<OntResource>> precedingFilter;
        Set<OntResource> previousStep = queryStep.resolvePreviousStep();
        precedingFilter = acceptableInput -> previousStep.isEmpty() ||
                OppModel.INSTANCE.areCompatible(acceptableInput, previousStep);

        // add BuiltinOperators
        addBuiltinOperators(position, result, context, typeFilter, precedingFilter);

        PsiElement originalPosition = parameters.getOriginalPosition();
        if (originalPosition == null) {
            return;
        }

        // add available callables
        List<PsiCallable> callables = file.listPsiCallables().stream()
                .filter(psiCallable -> file.isAccessible(originalPosition, psiCallable))
                .collect(Collectors.toList());
        addCallables(callables, result, typeFilter, precedingFilter, context);

//        // if inside a queries block, add the preceding sibling statements as callable options
//        addCallables(getFromSiblingDefined(position), result, typeFilter, precedingFilter, context);
//
//        // and all that are provided by the (if any) host file:
//        addCallables(getFromCallableProviders((ODTFile) parameters.getOriginalFile()), result, typeFilter, precedingFilter, context);
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
