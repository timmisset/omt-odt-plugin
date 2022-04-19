package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.ODTBaseScriptLine;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.Callable;

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

    @NotNull
    private LookupElementBuilder createLookupElement(Callable callable, String callId) {
        StringBuilder signature = new StringBuilder();
        if (callable.minNumberOfArguments() > 0) {
            signature.append("(");
            for (int i = 0; i < callable.maxNumberOfArguments(); i++) {
                if (i > 0) {
                    signature.append(", ");
                }
                signature.append("$param").append(i);
            }
            signature.append(")");
        } else {
            if (callable.isCommand()) {
                signature.append("()");
            }
        }
        String lookup = callId + signature;
        String typeText = callable.isVoid() ? "void" : TTLResourceUtil.describeUrisForLookupJoined(callable.resolve());
        return LookupElementBuilder.create(lookup)
                .withLookupString(callId)
                .withLookupString(callId.toLowerCase())
                .withLookupString(callable.getName())
                .withLookupString(callable.getName().toLowerCase())
                .withPresentableText(callId)
                .withTailText(signature.toString(), true)
                .withIcon(PlatformIcons.METHOD_ICON)
                .withTypeText(typeText);
    }

    protected List<Callable> getFromSiblingDefined(PsiElement element) {
        ODTDefineStatement defineStatement = PsiTreeUtil.getParentOfType(element, ODTDefineStatement.class);
        if (defineStatement == null) {
            return Collections.emptyList();
        }
        ODTBaseScriptLine scriptLine = (ODTBaseScriptLine) defineStatement.getParent();
        List<Callable> callables = new ArrayList<>();
        while (scriptLine != null) {
            scriptLine = PsiTreeUtil.getPrevSiblingOfType(scriptLine, ODTBaseScriptLine.class);
            if (scriptLine != null) {
                callables.add(scriptLine.getDefinedStatement());
            }
        }
        return callables;
    }

    protected List<PsiCallable> getFromCallableProviders(@NotNull ODTFile file) {
        LinkedHashMap<YAMLMapping, OMTCallableProvider> providers = file.getProviders(OMTCallableProvider.class, OMTCallableProvider.KEY);
        return providers.entrySet()
                .stream()
                .map(entry -> getFromCallableProviders(file, entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<PsiCallable> getFromCallableProviders(@NotNull ODTFile file,
                                                       YAMLMapping mapping,
                                                       @NotNull OMTCallableProvider provider) {
        HashMap<String, List<PsiCallable>> callableMap = provider.getCallableMap(mapping, file.getHost());
        return callableMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    protected void addCallables(@NotNull Collection<? extends Callable> callables,
                                @NotNull CompletionResultSet result,
                                Predicate<Set<OntResource>> typeFilter,
                                Predicate<Set<OntResource>> precedingFilter) {
        callables.stream()
                .filter(Objects::nonNull)
                .filter(selectionFilter)
                .filter(callable -> precedingFilter.test(callable.getAcceptableInputType()))
                .filter(callable -> {
                    Set<OntResource> resolve = callable.resolve();
                    return resolve.isEmpty() || typeFilter.test(resolve);
                })
                .map(this::getLookupElement)
                .filter(Objects::nonNull)
                .map(lookupElement -> PrioritizedLookupElement.withPriority(lookupElement, Callable.getValue()))
                .forEach(result::addElement);
    }
}
