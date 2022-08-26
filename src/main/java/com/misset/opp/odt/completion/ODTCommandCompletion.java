package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.commands.BuiltinCommands;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;

public class ODTCommandCompletion extends ODTCallCompletion {
    public static final Key<Boolean> HAS_AT_SYMBOL = new Key<>("HAS_AT_SYMBOL");

    public ODTCommandCompletion() {
        super(Callable::isCommand);

        extend(CompletionType.BASIC, or(FIRST_QUERY_STEP.andNot(AFTER_FIRST_QUERY_STEP), getAfterCommandPrefixPattern())
                , new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  @NotNull ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        PsiElement position = parameters.getPosition();
                        Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                        Predicate<Set<OntResource>> precedingFilter = resources -> true;
                        // check if completion was already triggered with @ symbol
                        context.put(HAS_AT_SYMBOL,
                                Optional.ofNullable(PsiTreeUtil.prevLeaf(position)).map(PsiElement::getText).map(s -> s.equals("@")).orElse(false)
                        );

                        // add BuiltinCommands:
                        addCallables(BuiltinCommands.builtinCommands.values(), result, typeFilter, precedingFilter, context);

                        // if inside a queries block, add the preceding sibling statements as callable options
                        addCallables(getFromSiblingDefined(position), result, typeFilter, precedingFilter, context);

                        // and all that are provided by the (if any) host file:
                        ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                        addCallables(getFromCallableProviders(originalFile), result, typeFilter, precedingFilter, context);

                        // and all local commands available at this point:
                        LinkedHashMap<YAMLPsiElement, OMTLocalCommandProvider> localCommandProviders = originalFile.getProviders(YAMLPsiElement.class, OMTLocalCommandProvider.class, OMTLocalCommandProvider.KEY);
                        addCallables(localCommandProviders.values()
                                .stream()
                                .map(OMTLocalCommandProvider::getLocalCommandsMap)
                                .map(HashMap::values)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()), result, typeFilter, precedingFilter, context);
                    }
                });
    }

}
