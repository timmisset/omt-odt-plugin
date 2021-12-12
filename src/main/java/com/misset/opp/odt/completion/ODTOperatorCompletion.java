package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.builtin.operators.BuiltinOperators;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public class ODTOperatorCompletion extends ODTCallCompletion {
    private static final PsiJavaElementPattern.Capture<PsiElement> QUERY_STEP_OPERATOR =
            psiElement()
                    .inside(ODTQueryStep.class);

    public ODTOperatorCompletion() {


        extend(CompletionType.BASIC, QUERY_STEP_OPERATOR, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                // add BuiltinOperators:
                BuiltinOperators.builtinOperators.values()
                        .stream()
                        .map(ODTOperatorCompletion.super::getLookupElement)
                        .forEach(result::addElement);

                // if inside a queries block, add the preceding sibling statements as callable options
                addSiblingDefined(position, result);

                // and all that are provided by the (if any) host file:
                addProviders((ODTFile) parameters.getOriginalFile(), result);
            }

            private void addSiblingDefined(PsiElement element, @NotNull CompletionResultSet result) {
                ODTDefineStatement defineStatement = PsiTreeUtil.getParentOfType(element, ODTDefineStatement.class);
                if (defineStatement == null) {
                    return;
                }
                ODTScriptLine scriptLine = (ODTScriptLine) defineStatement.getParent();
                while (scriptLine != null) {
                    scriptLine = PsiTreeUtil.getPrevSiblingOfType(scriptLine, ODTScriptLine.class);
                    if (scriptLine != null && scriptLine.getStatement() instanceof ODTDefineStatement) {
                        result.addElement(getLookupElement((Callable) scriptLine.getStatement()));
                    }
                }
            }

            private void addProviders(ODTFile file, @NotNull CompletionResultSet result) {
                LinkedHashMap<YAMLMapping, OMTCallableProvider> providers = file.getProviders(OMTCallableProvider.class, OMTCallableProvider.KEY);
                providers.forEach(
                        (mapping, omtCallableProvider) -> addProviders(file, mapping, omtCallableProvider, result)
                );
            }

            private void addProviders(ODTFile file,
                                      YAMLMapping mapping,
                                      OMTCallableProvider provider,
                                      @NotNull CompletionResultSet result) {
                HashMap<String, List<PsiElement>> callableMap = provider.getCallableMap(mapping, file.getHost());
                callableMap.values().stream()
                        .flatMap(Collection::stream)
                        .map(this::toCallable)
                        .filter(Objects::nonNull)
                        .map(ODTOperatorCompletion.super::getLookupElement)
                        .forEach(result::addElement);
            }

            private @Nullable PsiCallable toCallable(PsiElement element) {
                return PsiTreeUtil.getParentOfType(element, PsiCallable.class);
            }
        });
    }


}
