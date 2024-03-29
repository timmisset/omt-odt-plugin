package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.ContextFactory;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;
import static com.misset.opp.odt.completion.ODTSharedCompletion.TYPE_FILTER;
import static com.misset.opp.odt.completion.ODTSharedCompletion.sharedContext;

public class ODTOperatorCompletion extends ODTCallCompletion {
    private static final ElementPattern<PsiElement> BUILTIN_OPERATOR_STRICT =
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
                SharedProcessingContext sharedProcessingContext = sharedContext.get();
                if (sharedProcessingContext != null && sharedProcessingContext.get(TYPE_FILTER) != null) {
                    typeFilter = typeFilter.and(sharedProcessingContext.get(TYPE_FILTER));
                }

                ODTQueryStep queryStep = PsiTreeUtil.getParentOfType(position, ODTQueryStep.class, true);
                if (queryStep != null) {
                    addOperatorCompletions(parameters, result, position, typeFilter, queryStep);
                }
            }
        });
    }

    public void addOperatorCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull CompletionResultSet result,
                                       PsiElement position,
                                       Predicate<Set<OntResource>> typeFilter,
                                       ODTQueryStep queryStep) {
        PsiFile originalFile = parameters.getOriginalFile();
        if (!(originalFile instanceof ODTFile)) {
            return;
        }

        ODTFile file = (ODTFile) originalFile;
        Project project = originalFile.getProject();

        Predicate<Set<OntResource>> precedingFilter;
        Set<OntResource> previousStep = queryStep.resolvePreviousStep();
        precedingFilter = acceptableInput -> previousStep.isEmpty() ||
                OntologyModel.getInstance(position.getProject()).areCompatible(acceptableInput, previousStep);

        ODTCall call = PsiTreeUtil.getParentOfType(position, ODTCall.class);
        Context context = call != null ? ContextFactory.fromCall(call) : null;

        // add non-Psi operators
        List<Callable> callablesWithInput = file.listCallables().stream().filter(Callable::requiresInput).collect(Collectors.toList());
        List<Callable> callablesWithoutInput = file.listCallables().stream().filter(callable -> !callable.requiresInput()).collect(Collectors.toList());
        if (BUILTIN_OPERATOR_STRICT.accepts(position)) {
            addCallables(callablesWithInput, result, typeFilter, precedingFilter, context, project);
            if (!AFTER_FIRST_QUERY_STEP.accepts(position)) {
                addCallables(callablesWithoutInput, result, typeFilter, precedingFilter, context, project);
            }
        } else {
            addCallables(callablesWithoutInput, result, typeFilter, precedingFilter, context, project);
        }

        PsiElement originalPosition = parameters.getOriginalPosition();
        if (originalPosition == null) {
            return;
        }

        // add available callables
        List<PsiCallable> callables = file.listPsiCallables().stream()
                .filter(psiCallable -> file.isAccessible(originalPosition, psiCallable))
                .collect(Collectors.toList());
        addCallables(callables, result, typeFilter, precedingFilter, context, project);
    }
}
