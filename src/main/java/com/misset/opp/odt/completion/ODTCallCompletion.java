package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformIcons;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.CALLABLE;
import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.PRIORITY_CALLABLE;
import static com.misset.opp.odt.completion.ODTSharedCompletion.sharedContext;

public abstract class ODTCallCompletion extends CompletionContributor {

    private final Predicate<Callable> selectionFilter;

    protected ODTCallCompletion(Predicate<Callable> selectionFilter) {
        this.selectionFilter = selectionFilter;
    }

    private static void setInsertHandler(int numberOfParams, boolean callCompletionOnInsert, InsertionContext context) {
        // when the signature is added, move the caret into the signature
        if (numberOfParams > 0) {
            context.getEditor().getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
            if (callCompletionOnInsert) {
                context.setLaterRunnable(() -> new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(
                        context.getProject(),
                        context.getEditor()
                ));
            }
        }
    }

    public static void addPriorityCallable(Callable callable,
                                           CompletionResultSet result,
                                           Context context,
                                           Project project) {
        addCallable(callable, result, PRIORITY_CALLABLE, context, project);
    }

    private static void addCallable(Callable callable,
                                    @NotNull CompletionResultSet result,
                                    CompletionPatterns.COMPLETION_PRIORITY priority, Context context,
                                    Project project) {
        LookupElement lookupElement = createLookupElement(callable, context, project);
        if (lookupElement != null) {
            LookupElement withPriority = PrioritizedLookupElement.withPriority(lookupElement, priority.getValue());
            result.addElement(withPriority);
        }
    }

    private static LookupElementBuilder createLookupElement(Callable callable, Context context, Project project) {
        String callId = callable.getCallId();
        if (callId == null) {
            return null;
        }
        StringBuilder signature = new StringBuilder();
        Map<Integer, String> parameterNames = callable.getParameterNames();
        int numberOfParams = Math.max(callable.maxNumberOfArguments(), parameterNames.size());
        if (numberOfParams > 0) {
            signature.append("(");
            for (int i = 0; i < numberOfParams; i++) {
                if (i > 0) {
                    signature.append(", ");
                }
                if (parameterNames.containsKey(i)) {
                    signature.append(parameterNames.get(i));
                } else {
                    signature.append("$param").append(i);
                }
            }
            signature.append(")");
        } else {
            if (callable.isCommand()) {
                signature.append("()");
            }
        }
        String lookup = callId + (signature.length() > 0 ? "()" : "");
        String typeText = OntologyResourceUtil.getInstance(project).describeUrisForLookupJoined(callable.resolve(context));
        return LookupElementBuilder.create(lookup)
                .withLookupString(callId)
                .withLookupString(callId.toLowerCase())
                .withLookupString(callable.getName())
                .withLookupString(callable.getName().toLowerCase())
                .withPresentableText(callId)
                .withTailText(signature.toString(), true)
                .withIcon(PlatformIcons.METHOD_ICON)
                .withInsertHandler((insertionContext, item) -> setInsertHandler(numberOfParams, callable.callCompletionOnInsert(), insertionContext))
                .withTypeText(typeText);
    }

    protected void addCallables(@NotNull Collection<? extends Callable> callables,
                                @NotNull CompletionResultSet result,
                                Predicate<Set<OntResource>> typeFilter,
                                Predicate<Set<OntResource>> precedingFilter,
                                Context context,
                                Project project) {
        Predicate<Callable> callableFilter = selectionFilter;
        SharedProcessingContext sharedProcessingContext = sharedContext.get();
        if (sharedProcessingContext != null && sharedProcessingContext.get(ODTSharedCompletion.CALLABLE_FILTER) != null) {
            callableFilter = callableFilter.and(sharedProcessingContext.get(ODTSharedCompletion.CALLABLE_FILTER));
        }

        callables.stream()
                .filter(Objects::nonNull)
                .filter(callableFilter)
                .filter(callable -> precedingFilter.test(callable.getAcceptableInputType()))
                .filter(callable -> {
                    Set<OntResource> resolve = callable.resolve(context);
                    return resolve.isEmpty() || typeFilter.test(resolve);
                })
                .forEach(callable -> addCallable(callable, result, CALLABLE, context, project));
    }
}
