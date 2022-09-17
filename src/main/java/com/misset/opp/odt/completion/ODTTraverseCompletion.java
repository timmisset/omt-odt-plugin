package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.OntologyTraverseDirection;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;
import static com.misset.opp.odt.completion.ODTSharedCompletion.sharedContext;

public class ODTTraverseCompletion extends CompletionContributor {
    private static final ElementPattern<PsiElement> TRAVERSE =
            or(AFTER_FIRST_QUERY_STEP, INSIDE_DEFINED_QUERY, INSIDE_QUERY_FILTER);

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
                Predicate<Set<OntResource>> firstTypeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);
                SharedProcessingContext sharedProcessingContext = sharedContext.get();
                if (sharedProcessingContext != null && sharedProcessingContext.get(ODTSharedCompletion.TYPE_FILTER) != null) {
                    firstTypeFilter = firstTypeFilter.and(sharedProcessingContext.get(ODTSharedCompletion.TYPE_FILTER));
                }

                // add model options
                addModelTraverseLookupElements(
                        subjects,
                        getForwardPredicates(firstTypeFilter, subjects),
                        OntologyTraverseDirection.TraverseDirection.FORWARD,
                        availableNamespaces,
                        result,
                        false);
                addModelTraverseLookupElements(
                        subjects,
                        getReversePredicates(firstTypeFilter, subjects),
                        OntologyTraverseDirection.TraverseDirection.REVERSE,
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
            predicates.put(OntologyModelConstants.getRdfType(), Collections.emptySet());
        } else {
            Set<Property> properties = OntologyModel.getInstance().listReversePredicates(subjects);
            for (Property property : properties) {
                Set<OntResource> predicateSubjects = OntologyModel.getInstance().listSubjects(property, subjects);
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
            predicates.put(OntologyModelConstants.getRdfType(), Collections.emptySet());
        } else {
            Set<Property> properties = OntologyModel.getInstance().listPredicates(subjects);
            for (Property property : properties) {
                Set<OntResource> predicateObjects = OntologyModel.getInstance().listObjects(subjects, property);
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
            OntologyTraverseDirection.TraverseDirection direction,
            Map<String, String> availableNamespaces,
            CompletionResultSet result,
            boolean rootIndicator) {
        predicates.keySet()
                .forEach(predicate -> {
                    String curie = OntologyResourceUtil.parseToCurie(predicate, availableNamespaces);
                    if (direction == OntologyTraverseDirection.TraverseDirection.REVERSE) {
                        curie = "^" + curie;
                    } else if (rootIndicator) {
                        curie = "/" + curie;
                    }

                    LookupElementBuilder predicateLookupElement =
                            OntologyResourceUtil.getPredicateLookupElement(subjects, predicate, predicates.get(predicate), direction, curie);

                    LookupElement withPriority =
                            PrioritizedLookupElement.withPriority(predicateLookupElement, COMPLETION_PRIORITY.TRAVERSE.getValue());

                    result.addElement(withPriority);
                });
    }

}
