package com.misset.opp.odt.refactoring;

import com.intellij.openapi.util.text.Strings;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQueryOperationStep;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ODTNameSuggestionProvider implements NameSuggestionProvider {
    @Override
    public @Nullable SuggestedNameInfo getSuggestedNames(@NotNull PsiElement element,
                                                         @Nullable PsiElement nameSuggestionContext,
                                                         @NotNull Set<String> result) {
        if (!element.getLanguage().isKindOf(ODTLanguage.INSTANCE)) {
            return null;
        }

        List<String> suggestions = new ArrayList<>();
        if (element instanceof ODTVariable) {
            suggestions.addAll(getVariableNameSuggestions((ODTVariable) element));
        }
        if (element instanceof ODTResolvable) {
            suggestions.addAll(forVariables(getTypeSuggestions((Resolvable) element)));
        }
        if (element instanceof ODTQueryStatement && element.getFirstChild() instanceof ODTResolvableQueryPath) {
            List<ODTResolvableQueryOperationStep> operationStepList = ((ODTResolvableQueryPath) element.getFirstChild()).getResolvableQueryOperationStepList();
            if (!operationStepList.isEmpty()) {
                ODTResolvableQueryOperationStep operationStep = operationStepList.get(operationStepList.size() - 1);
                ODTQueryStep queryStep = operationStep.getQueryStep();
                if (queryStep instanceof ODTCall) {
                    ODTCall call = (ODTCall) queryStep;
                    suggestions.addAll(forVariables(Collections.singletonList(call.getName())));
                }
            }
        }
        if (element instanceof ODTCommandCall) {
            ODTCall call = (ODTCall) element;
            suggestions.addAll(forVariables(Collections.singletonList(call.getName())));
        }
        suggestions = suggestions.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());

        if (suggestions.isEmpty()) {
            return null;
        }
        result.addAll(suggestions);
        return new SuggestedNameInfo(suggestions.toArray(String[]::new)) {
            @Override
            public void nameChosen(String name) {
                // do nothing but cannot be left as-is according to comments in super method
            }
        };
    }

    private List<String> getVariableNameSuggestions(ODTVariable variable) {
        // the variable type is included via the Resolvable suggestions
        // but first, let's suggest a name based on the assignment part callable (if any):
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(variable, ODTVariableAssignment.class))
                .filter(variableAssignment -> variableAssignment.getVariableList().indexOf(variable) == 0)
                .map(ODTVariableAssignment::getVariableValue)
                .map(ODTVariableValue::getStatement)
                .map(this::getNameSuggestions)
                .orElse(Collections.emptyList());

    }


    private List<String> forVariables(List<String> names) {
        return names.stream()
                .filter(s -> !s.isBlank())
                .map(s -> s.startsWith("$") ? s : "$" + s)
                .collect(Collectors.toList());
    }

    private List<String> getNameSuggestions(ODTStatement statement) {
        if (statement instanceof ODTQuery) {
            return getNameSuggestions((ODTQuery) statement);
        } else if (statement instanceof ODTCommandCall) {
            return getNameSuggestions((ODTCommandCall) statement);
        } else {
            return Collections.emptyList();
        }
    }

    private List<String> getNameSuggestions(ODTQuery query) {
        if (query instanceof ODTResolvableQueryPath) {
            return Optional.of((ODTResolvableQueryPath) query)
                    .map(ODTResolvableQueryPath::getResolvableQueryOperationStepList)
                    .map(steps -> steps.get(steps.size() - 1))
                    .map(ODTQueryOperationStep::getQueryStep)
                    .filter(ODTOperatorCall.class::isInstance)
                    .map(step -> ((ODTOperatorCall) step).getName())
                    .map(this::getNameSuggestions)
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    private List<String> getNameSuggestions(String stringWithCasing) {
        String[] strings = stringWithCasing.split("(?=\\p{Lu})");
        // getMyQueryResult == get|My|Query|Result
        // -> result
        // -> queryResult()
        // -> myQueryResult()
        // -> getMyQueryResult()
        List<String> result = new ArrayList<>();
        for (int i = strings.length; i >= 0; i--) {
            String nameSuggestion = Strings.join(Arrays.copyOfRange(strings, i, strings.length));
            result.add(camelCase(nameSuggestion));
        }
        return result;
    }

    private List<String> getNameSuggestions(ODTCommandCall call) {
        return List.of(camelCase(call.getName()));
    }

    private List<String> getTypeSuggestions(Resolvable resolvable) {
        return resolvable.resolve().stream()
                .map(OppModel.getInstance()::toClass)
                .map(this::getSuperClasses)
                .flatMap(Collection::stream)
                .distinct()
                .filter(Objects::nonNull)
                .filter(s -> s.length() > 2)
                .map(this::camelCase)
                .collect(Collectors.toList());
    }

    private List<String> getSuperClasses(OntClass ontClass) {
        if (ontClass == null) {
            return Collections.emptyList();
        }
        List<OntClass> ontClasses = ontClass.listSuperClasses().toList();
        List<String> classLocalNames = new ArrayList<>();
        classLocalNames.add(ontClass.getLocalName());
        classLocalNames.addAll(ontClasses.stream()
                .filter(superClass -> superClass != OppModelConstants.getOwlClass() &&
                        superClass != OppModelConstants.getOwlThingClass())
                .map(Resource::getLocalName)
                .collect(Collectors.toList()));
        return classLocalNames;
    }

    private String camelCase(String name) {
        if (name.length() < 2) {
            return name.toLowerCase();
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
