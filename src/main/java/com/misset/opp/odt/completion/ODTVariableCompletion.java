package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.variable.ODTBaseVariable;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlVariableDelegate;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.global.GlobalVariable;
import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.shared.providers.CallableLocalVariableTypeProvider;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ODTVariableCompletion extends CompletionContributor {

    public ODTVariableCompletion() {
        extend(CompletionType.BASIC, CompletionPatterns.FIRST_QUERY_STEP, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);

                ODTFile originalFile = (ODTFile) parameters.getOriginalFile();

                PsiTreeUtil.getParentOfType(position, ODTTypeFilterProvider.class);

                addVariables(result, position, typeFilter, originalFile);
            }
        });
    }

    public void addBooleanCompletions(@NotNull CompletionParameters parameters,
                                      @NotNull CompletionResultSet result,
                                      PsiElement element) {
        ODTFile originalFile = (ODTFile) parameters.getOriginalFile();

        PsiTreeUtil.getParentOfType(element, ODTTypeFilterProvider.class);

        Predicate<Set<OntResource>> filterType = resources -> OppModel.INSTANCE.areCompatible(Collections.singleton(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE), resources);
        addVariables(result, element, filterType, originalFile);
    }

    public void addVariables(@NotNull CompletionResultSet result,
                             PsiElement position,
                             Predicate<Set<OntResource>> typeFilter,
                             ODTFile originalFile) {
        // add global variables
        addVariables(GlobalVariable.getVariables(), typeFilter, result);

        // add variables declared in this file:
        // do not use the original file here, the relationship util matches on common parents
        // and the original file is not the same as the (temp) completion file
        addVariables(PsiTreeUtil.findChildrenOfType(position.getContainingFile(), ODTBaseVariable.class)
                .stream()
                .filter(odtVariable -> PsiRelationshipUtil.canBeRelatedElement(odtVariable, position))
                .collect(Collectors.toList()), typeFilter, result);

        // add local variables from commands ($value, $index, $array):
        addVariables(getLocalVariablesForCall(position), typeFilter, result);

        // add local variables from host providers ($newValue, $oldValue):
        addVariables(getLocalVariableProviders(originalFile), typeFilter, result);

        // add declared variables from other host providers
        addVariables(getDeclaredVariableProviders(originalFile), typeFilter, result);
    }

    private void addVariables(Collection<? extends Variable> variables,
                              Predicate<Set<OntResource>> typeFilter,
                              @NotNull CompletionResultSet result) {
        variables.stream()
                .filter(variable -> typeFilter.test(variable.resolve()))
                .map(this::getLookupElement)
                .map(lookupElement -> PrioritizedLookupElement.withPriority(lookupElement, CompletionPatterns.COMPLETION_PRIORITY.Variable.getValue()))
                .forEach(result::addElement);
    }

    private List<Variable> getLocalVariablesForCall(PsiElement element) {
        return PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                .stream()
                .map(call -> getLocalVariablesForCall(call, element))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }

    private List<LocalVariable> getLocalVariablesForCall(ODTCall call, PsiElement element) {
        Callable callable = call.getCallable();
        if (callable instanceof CallableLocalVariableTypeProvider) {
            return ((CallableLocalVariableTypeProvider) callable).getLocalVariables(call, call.getArgumentIndexOf(element));
        }
        return Collections.emptyList();
    }

    private List<OMTYamlVariableDelegate> getDeclaredVariableProviders(ODTFile originalFile) {
        // add variables from host provider:
        LinkedHashMap<YAMLMapping, OMTVariableProvider> providers = originalFile.getProviders(OMTVariableProvider.class, OMTVariableProvider.KEY);
        return providers.entrySet().stream()
                .map(entry -> getDeclaredVariableProviders(entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<OMTYamlVariableDelegate> getDeclaredVariableProviders(YAMLMapping mapping,
                                                                       OMTVariableProvider provider) {
        HashMap<String, List<PsiElement>> variableMap = provider.getVariableMap(mapping);
        return variableMap.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(YAMLPlainTextImpl.class::isInstance)
                .map(YAMLPlainTextImpl.class::cast)
                .map(OMTYamlDelegateFactory::createDelegate)
                .map(OMTYamlVariableDelegate.class::cast)
                .collect(Collectors.toList());
    }

    private List<LocalVariable> getLocalVariableProviders(ODTFile originalFile) {
        // add variables from host provider:
        LinkedHashMap<YAMLValue, OMTLocalVariableProvider> providers = originalFile.getProviders(YAMLValue.class, OMTLocalVariableProvider.class, OMTLocalVariableProvider.KEY);
        return providers.entrySet()
                .stream()
                .map(entrySet -> entrySet.getValue().getLocalVariableMap(entrySet.getKey()))
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private @NotNull LookupElementBuilder getLookupElement(Variable variable) {
        return LookupElementBuilder.create(variable.getName())
                .withLookupString(variable.getName().substring(1))
                .withLookupString(variable.getName().substring(1).toLowerCase())
                .withIcon(variable.isParameter() ? PlatformIcons.PARAMETER_ICON : PlatformIcons.VARIABLE_ICON)
                .withTypeText(TTLResourceUtil.describeUrisForLookupJoined(variable.resolve()));
    }

}
