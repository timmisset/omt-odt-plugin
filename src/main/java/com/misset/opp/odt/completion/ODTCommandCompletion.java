package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.commands.BuiltinCommands;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.misset.opp.odt.completion.CompletionPatterns.AFTER_FIRST_QUERY_STEP;
import static com.misset.opp.odt.completion.CompletionPatterns.FIRST_QUERY_STEP;

public class ODTCommandCompletion extends ODTCallCompletion {

    public ODTCommandCompletion() {
        super(Callable::isCommand);

        extend(CompletionType.BASIC, FIRST_QUERY_STEP.andNot(AFTER_FIRST_QUERY_STEP), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                Predicate<Set<OntResource>> precedingFilter = resources -> true;

                // add BuiltinCommands:
                addCallables(BuiltinCommands.builtinCommands.values(), result, typeFilter, precedingFilter);

                // if inside a queries block, add the preceding sibling statements as callable options
                addCallables(getFromSiblingDefined(position), result, typeFilter, precedingFilter);

                // and all that are provided by the (if any) host file:
                ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                addCallables(getFromCallableProviders(originalFile), result, typeFilter, precedingFilter);

                // and all local commands available at this point:
                LinkedHashMap<YAMLPsiElement, OMTLocalCommandProvider> localCommandProviders = originalFile.getProviders(YAMLPsiElement.class, OMTLocalCommandProvider.class, OMTLocalCommandProvider.KEY);
                addCallables(localCommandProviders.values()
                        .stream()
                        .map(OMTLocalCommandProvider::getLocalCommandsMap)
                        .map(HashMap::values)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()), result, typeFilter, precedingFilter);
            }
        });
    }


}
