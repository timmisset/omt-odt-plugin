package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;

public class ODTTraverseCompletion extends CompletionContributor {
    private static final ElementPattern<PsiElement> TRAVERSE =
            or(AFTER_FIRST_QUERY_STEP, INSIDE_DEFINED_QUERY, INSIDE_QUERY_FILTER);

    public enum TraverseDirection {
        FORWARD, REVERSE
    }

    public ODTTraverseCompletion() {
        // the position rules where a Traverse and QueryOperator can be suggested is identical
        extend(CompletionType.BASIC, TRAVERSE, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                if (!(parameters.getOriginalFile() instanceof ODTFile)) {
                    return;
                }
                final ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                final Map<String, String> availableNamespaces = originalFile.getAvailableNamespaces();

                PsiElement position = parameters.getPosition();
                result = result.withPrefixMatcher(ODTCompletionUtil.getCuriePrefixMatcher(parameters, result, ODTQueryOperationStep.class));

                final Set<OntResource> subjects = Optional.of(position)
                        .map(element -> PsiTreeUtil.getParentOfType(element, ODTQueryOperationStep.class))
                        .map(ODTQueryOperationStep::resolvePreviousStep)
                        .orElse(Collections.emptySet());

                // add model options
                addModelTraverseLookupElements(
                        subjects,
                        getForwardPredicates(ODTTypeFilterProvider.getFirstTypeFilter(position), subjects),
                        TraverseDirection.FORWARD,
                        availableNamespaces,
                        result,
                        false);
                addModelTraverseLookupElements(
                        subjects,
                        getReversePredicates(ODTTypeFilterProvider.getFirstTypeFilter(position), subjects),
                        TraverseDirection.REVERSE,
                        availableNamespaces,
                        result,
                        false);

            }
        });
    }

    public static Map<Property, Set<OntResource>> getReversePredicates(Predicate<Set<OntResource>> typeFilter,
                                                                       Set<OntResource> subjects) {
        HashMap<Property, Set<OntResource>> predicates = new HashMap<>();
        if (subjects.isEmpty()) {
            predicates.put(OppModelConstants.getRdfType(), Collections.emptySet());
        } else {
            Set<Property> properties = OppModel.getInstance().listReversePredicates(subjects);
            for (Property property : properties) {
                Set<OntResource> predicateSubjects = OppModel.getInstance().listSubjects(property, subjects);
                if (typeFilter.test(predicateSubjects)) {
                    predicates.put(property, predicateSubjects);
                }
            }
        }
        return predicates;
    }

    public static Map<Property, Set<OntResource>> getForwardPredicates(Predicate<Set<OntResource>> typeFilter,
                                                                       Set<OntResource> subjects) {
        HashMap<Property, Set<OntResource>> predicates = new HashMap<>();
        if (subjects.isEmpty()) {
            predicates.put(OppModelConstants.getRdfType(), Collections.emptySet());
        } else {
            Set<Property> properties = OppModel.getInstance().listPredicates(subjects);
            for (Property property : properties) {
                Set<OntResource> predicateObjects = OppModel.getInstance().listObjects(subjects, property);
                if (typeFilter.test(predicateObjects)) {
                    predicates.put(property, predicateObjects);
                }
            }
        }
        return predicates;
    }

    public static void addModelTraverseLookupElements(
            Set<OntResource> subjects,
            Map<Property, Set<OntResource>> predicates,
            TraverseDirection direction,
            Map<String, String> availableNamespaces,
            CompletionResultSet result,
            boolean rootIndicator) {
        predicates.keySet()
                .forEach(predicate -> {
                    String curie = TTLResourceUtil.parseToCurie(predicate, availableNamespaces);
                    if (direction == TraverseDirection.REVERSE) {
                        curie = "^" + curie;
                    } else if (rootIndicator) {
                        curie = "/" + curie;
                    }

                    LookupElementBuilder predicateLookupElement =
                            TTLResourceUtil.getPredicateLookupElement(subjects, predicate, predicates.get(predicate), direction, curie);

                    LookupElement withPriority =
                            PrioritizedLookupElement.withPriority(predicateLookupElement, COMPLETION_PRIORITY.TRAVERSE.getValue());

                    result.addElement(withPriority);
                });
    }

}
