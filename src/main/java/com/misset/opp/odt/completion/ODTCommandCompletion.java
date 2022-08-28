package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                        if (!(parameters.getOriginalFile() instanceof ODTFile)) {
                            return;
                        }
                        ODTFile file = (ODTFile) parameters.getOriginalFile();

                        Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                        Predicate<Set<OntResource>> precedingFilter = resources -> true;
                        // check if completion was already triggered with @ symbol
                        context.put(HAS_AT_SYMBOL,
                                Optional.ofNullable(PsiTreeUtil.prevLeaf(position)).map(PsiElement::getText).map(s -> s.equals("@")).orElse(false)
                        );

                        // add non-psi callables:
                        addCallables(file.listCallables(), result, typeFilter, precedingFilter, context);

                        PsiElement originalPosition = parameters.getOriginalPosition();
                        if (originalPosition == null) {
                            return;
                        }
                        // add available callables
                        List<PsiCallable> callables = file.listPsiCallables().stream()
                                .filter(psiCallable -> file.isAccessible(originalPosition, psiCallable))
                                .collect(Collectors.toList());
                        addCallables(callables, result, typeFilter, precedingFilter, context);
//
//                        // and all that are provided by the (if any) host file:
//
//                        addCallables(file.getCallables(), result, typeFilter, precedingFilter, context);
//
//                        // and all local commands available at this point:
//                        LinkedHashMap<YAMLPsiElement, OMTLocalCommandProvider> localCommandProviders = file.getProviders(YAMLPsiElement.class, OMTLocalCommandProvider.class, OMTLocalCommandProvider.KEY);
//                        addCallables(localCommandProviders.values()
//                                .stream()
//                                .map(OMTLocalCommandProvider::getLocalCommandsMap)
//                                .map(HashMap::values)
//                                .flatMap(Collection::stream)
//                                .collect(Collectors.toList()), result, typeFilter, precedingFilter, context);
                    }
                });
    }

}
