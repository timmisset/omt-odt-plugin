package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.Variable;
import com.misset.opp.callable.global.GlobalVariable;
import com.misset.opp.callable.local.CallableLocalVariableTypeProvider;
import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.variable.ODTBaseVariable;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlVariableDelegate;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public class ODTVariableCompletion extends ODTCallCompletion {
    private static final PsiJavaElementPattern.Capture<PsiElement> QUERY_STEP_OPERATOR =
            psiElement()
                    .inside(ODTQueryStep.class);

    public ODTVariableCompletion() {


        extend(CompletionType.BASIC, QUERY_STEP_OPERATOR, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                // add global variables
                GlobalVariable.getVariables()
                        .stream()
                        .map(this::getLookupElement)
                        .forEach(result::addElement);

                // add variables declared in this file:
                // do not use the original file here, the relationship util matches on common parents
                // and the original file is not the same as the (temp) completion file
                PsiTreeUtil.findChildrenOfType(position.getContainingFile(), ODTBaseVariable.class)
                        .stream()
                        .filter(odtVariable -> PsiRelationshipUtil.canBeRelatedElement(odtVariable, position))
                        .map(this::getLookupElement)
                        .forEach(result::addElement);

                // add local variables from commands ($value, $index, $array):
                addLocalVariablesForCall(position, result);

                // add local variables from host providers ($newValue, $oldValue):
                addLocalVariableProviders(originalFile, result);

                // add declared variables from other host providers
                addDeclaredVariableProviders(originalFile, result);
            }

            private void addLocalVariablesForCall(PsiElement element, @NotNull CompletionResultSet result) {
                PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                        .stream()
                        .map(call -> getLocalVariablesForCall(call, element))
                        .flatMap(Collection::parallelStream)
                        .map(this::getLookupElement)
                        .forEach(result::addElement);
            }

            private List<LocalVariable> getLocalVariablesForCall(ODTCall call, PsiElement element) {
                Callable callable = call.getCallable();
                if (callable instanceof CallableLocalVariableTypeProvider) {
                    return ((CallableLocalVariableTypeProvider) callable).getLocalVariables(call, call.getArgumentIndexOf(element));
                }
                return Collections.emptyList();
            }

            private void addDeclaredVariableProviders(ODTFile originalFile, @NotNull CompletionResultSet result) {
                // add variables from host provider:
                LinkedHashMap<YAMLMapping, OMTVariableProvider> providers = originalFile.getProviders(OMTVariableProvider.class, OMTVariableProvider.KEY);
                providers.forEach(
                        (mapping, omtVariableProvider) -> addDeclaredVariableProviders(mapping, omtVariableProvider, result)
                );
            }

            private void addDeclaredVariableProviders(YAMLMapping mapping,
                                                      OMTVariableProvider provider,
                                                      @NotNull CompletionResultSet result) {
                HashMap<String, List<PsiElement>> variableMap = provider.getVariableMap(mapping);
                variableMap.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .filter(YAMLPlainTextImpl.class::isInstance)
                        .map(YAMLPlainTextImpl.class::cast)
                        .map(OMTYamlVariableDelegate::new)
                        .map(this::getLookupElement)
                        .forEach(result::addElement);
            }

            private void addLocalVariableProviders(ODTFile originalFile, @NotNull CompletionResultSet result) {
                // add variables from host provider:
                LinkedHashMap<YAMLValue, OMTLocalVariableProvider> providers = originalFile.getProviders(YAMLValue.class, OMTLocalVariableProvider.class, OMTLocalVariableProvider.KEY);
                providers.entrySet()
                        .stream()
                        .map(entrySet -> entrySet.getValue().getLocalVariableMap(entrySet.getKey()))
                        .map(Map::values)
                        .flatMap(Collection::stream)
                        .map(this::getLookupElement)
                        .forEach(result::addElement);
            }

            private @NotNull LookupElementBuilder getLookupElement(Variable variable) {
                return LookupElementBuilder.create(variable.getName())
                        .withLookupString(variable.getName().substring(1))
                        .withLookupString(variable.getName().substring(1).toLowerCase())
                        .withIcon(variable.isParameter() ? PlatformIcons.PARAMETER_ICON : PlatformIcons.VARIABLE_ICON)
                        .withTypeText(TTLResourceUtil.describeUrisForLookupJoined(variable.getType()));
            }
        });
    }


}
