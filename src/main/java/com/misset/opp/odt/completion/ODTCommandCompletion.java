package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

                        result = result.withPrefixMatcher(getPrefixMatcher(parameters, result));

                        Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                        Predicate<Set<OntResource>> precedingFilter = resources -> true;

                        // add non-psi callables:
                        addCallables(file.listCallables(), result, typeFilter, precedingFilter);

                        PsiElement originalPosition = parameters.getOriginalPosition();
                        if (originalPosition == null) {
                            return;
                        }
                        // add available callables
                        List<PsiCallable> callables = file.listPsiCallables().stream()
                                .filter(psiCallable -> file.isAccessible(originalPosition, psiCallable))
                                .collect(Collectors.toList());
                        addCallables(callables, result, typeFilter, precedingFilter);
                    }
                });
    }

    private String getPrefixMatcher(@NotNull CompletionParameters parameters,
                                    @NotNull CompletionResultSet result) {
        String substring = ODTCompletionUtil.getPrefixMatcherSubstring(parameters, ODTCall.class);
        if (substring == null) {
            substring = "";
        }
        substring = substring + result.getPrefixMatcher().getPrefix();
        return substring;
    }

}
