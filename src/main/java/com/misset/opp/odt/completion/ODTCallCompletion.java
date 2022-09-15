package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.PlatformIcons;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.CALLABLE;
import static com.misset.opp.odt.completion.ODTSharedCompletion.sharedContext;

public abstract class ODTCallCompletion extends CompletionContributor {

    private final Predicate<Callable> selectionFilter;

    protected ODTCallCompletion(Predicate<Callable> selectionFilter) {
        this.selectionFilter = selectionFilter;
    }

    protected LookupElement getLookupElement(Callable callable) {
        return Optional.ofNullable(callable.getCallId())
                .map(s -> createLookupElement(callable, s))
                .orElse(null);
    }

    private LookupElementBuilder createLookupElement(Callable callable, String callId) {
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
        String typeText = callable.isVoid() ? "void" : TTLResourceUtil.describeUrisForLookupJoined(callable.resolve());
        return LookupElementBuilder.create(lookup)
                .withLookupString(callId)
                .withLookupString(callId.toLowerCase())
                .withLookupString(callable.getName())
                .withLookupString(callable.getName().toLowerCase())
                .withPresentableText(callId)
                .withTailText(signature.toString(), true)
                .withIcon(PlatformIcons.METHOD_ICON)
                .withInsertHandler((context, item) -> setInsertHandler(numberOfParams, callable.callCompletionOnInsert(), context))
                .withTypeText(typeText);
    }

    private void setInsertHandler(int numberOfParams, boolean callCompletionOnInsert, InsertionContext context) {
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

    protected void addCallables(@NotNull Collection<? extends Callable> callables,
                                @NotNull CompletionResultSet result,
                                Predicate<Set<OntResource>> typeFilter,
                                Predicate<Set<OntResource>> precedingFilter) {
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
                    Set<OntResource> resolve = callable.resolve();
                    return resolve.isEmpty() || typeFilter.test(resolve);
                })
                .forEach(callable -> addCallable(callable, result));
    }

    private void addCallable(Callable callable,
                             @NotNull CompletionResultSet result) {
        LookupElement lookupElement = getLookupElement(callable);
        if (lookupElement != null) {
            LookupElement withPriority = PrioritizedLookupElement.withPriority(lookupElement, CALLABLE.getValue());
            result.addElement(withPriority);
        }
    }
}
