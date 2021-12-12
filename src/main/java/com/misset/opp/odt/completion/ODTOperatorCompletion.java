package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.callable.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;

public class ODTOperatorCompletion extends ODTCallCompletion {
    ElementPattern<PsiElement> BUILTIN_OPERATOR =
            or(AFTER_FIRST_QUERY_STEP, INSIDE_DEFINED_QUERY, INSIDE_QUERY_FILTER);

    public ODTOperatorCompletion() {
        super(callable -> !callable.isCommand());

        extend(CompletionType.BASIC, QUERY_STEP, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);

                // add BuiltinOperators
                // a bit more strict, they are usually applied on the previous step in a query
                if (BUILTIN_OPERATOR.accepts(position)) {
                    addCallables(BuiltinOperators.builtinOperators.values(), result, typeFilter);
                }

                // if inside a queries block, add the preceding sibling statements as callable options
                addCallables(getFromSiblingDefined(position), result, typeFilter);

                // and all that are provided by the (if any) host file:
                addCallables(getFromCallableProviders((ODTFile) parameters.getOriginalFile()), result, typeFilter);
            }
        });
    }
}
