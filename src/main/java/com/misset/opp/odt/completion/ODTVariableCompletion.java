package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.variable.ODTBaseVariable;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiVariable;
import com.misset.opp.shared.providers.CallableLocalVariableTypeProvider;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.misset.opp.omt.completion.OMTODTInjectableSectionCompletion.TYPE_FILTER;
import static com.misset.opp.omt.completion.OMTODTInjectableSectionCompletion.sharedContext;

public class ODTVariableCompletion extends CompletionContributor {
    public ODTVariableCompletion() {
        extend(CompletionType.BASIC, CompletionPatterns.FIRST_QUERY_STEP, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                SharedProcessingContext sharedProcessingContext = sharedContext.get();
                if (sharedProcessingContext != null && sharedProcessingContext.get(TYPE_FILTER) != null) {
                    typeFilter = typeFilter.and(sharedProcessingContext.get(TYPE_FILTER));
                }

                if (!(parameters.getOriginalFile() instanceof ODTFile)) {
                    return;
                }
                ODTFile file = (ODTFile) parameters.getOriginalFile();

                // add variables declared in this file:
                // do not use the original file here, the relationship util matches on common parents
                // and the original file is not the same as the (temp) completion file
                addVariables(PsiTreeUtil.findChildrenOfType(position.getContainingFile(), ODTBaseVariable.class)
                        .stream()
                        .filter(odtVariable -> PsiRelationshipUtil.canBeRelatedElement(odtVariable, position))
                        .collect(Collectors.toList()), typeFilter, result);

                // add all non-psi variables declared in the scope of the ODT file
                addVariables(file.listVariables(), typeFilter, result);

                // add local variables from commands such as $value, $index, $array:
                addVariables(getLocalVariablesForCall(position), typeFilter, result);

                List<PsiVariable> variables = file.listPsiVariables().stream()
                        .filter(psiVariable -> file.isAccessible(position, psiVariable))
                        .collect(Collectors.toList());

                addVariables(variables, typeFilter, result);
            }
        });
    }

    private void addVariables(Collection<? extends Variable> variables,
                              Predicate<Set<OntResource>> typeFilter,
                              @NotNull CompletionResultSet result) {
        variables.stream()
                .filter(variable -> typeFilter.test(variable.resolve()))
                .map(this::getLookupElement)
                .forEach(result::addElement);
    }

    private List<Variable> getLocalVariablesForCall(PsiElement element) {
        return PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                .stream()
                .map(call -> getLocalVariablesForCall(call, element))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }

    private List<Variable> getLocalVariablesForCall(ODTCall call, PsiElement element) {
        Callable callable = call.getCallable();
        if (callable instanceof CallableLocalVariableTypeProvider) {
            return ((CallableLocalVariableTypeProvider) callable).getLocalVariables(call, call.getArgumentIndexOf(element));
        }
        return Collections.emptyList();
    }

    private @NotNull LookupElement getLookupElement(Variable variable) {
        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(variable.getName())
                .withLookupString(variable.getName().substring(1))
                .withLookupString(variable.getName().substring(1).toLowerCase())
                .withTailText("  " + variable.getSource(), true)
                .withIcon(variable.isParameter() ? PlatformIcons.PARAMETER_ICON : PlatformIcons.VARIABLE_ICON)
                .withTypeText(TTLResourceUtil.describeUrisForLookupJoined(variable.resolve()));

        double priority = variable.getScope() == Variable.Scope.GLOBAL ?
                CompletionPatterns.COMPLETION_PRIORITY.VARIABLE_GLOBAL.getValue() :
                CompletionPatterns.COMPLETION_PRIORITY.VARIABLE_LOCAL.getValue();
        return PrioritizedLookupElement.withPriority(lookupElementBuilder, priority);
    }

}
