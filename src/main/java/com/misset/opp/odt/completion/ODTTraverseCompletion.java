package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryOperationStep;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.intellij.patterns.StandardPatterns.or;
import static com.misset.opp.odt.completion.CompletionPatterns.*;
import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.Traverse;

public class ODTTraverseCompletion extends CompletionContributor {
    ElementPattern<PsiElement> TRAVERSE =
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
                final ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                final Map<String, String> availableNamespaces = originalFile.getAvailableNamespaces();

                PsiElement position = parameters.getPosition();
                Predicate<Set<OntResource>> typeFilter = ODTTypeFilterProvider.getFirstTypeFilter(position);

                final Set<OntResource> subjects = Optional.of(position)
                        .map(element -> PsiTreeUtil.getParentOfType(element, ODTResolvableQueryOperationStep.class))
                        .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                        .orElse(Collections.emptySet());
                Set<Property> forwardPredicates;
                Set<Property> reversePredicates;
                if (subjects.isEmpty()) {
                    // cannot establish subject, only show the rdf:type predicates
                    forwardPredicates = Set.of(OppModel.INSTANCE.RDF_TYPE);
                    reversePredicates = Set.of(OppModel.INSTANCE.RDF_TYPE);
                } else {
                    forwardPredicates = OppModel.INSTANCE.listPredicates(subjects).stream()
                            .filter(property -> typeFilter.test(OppModel.INSTANCE.listObjects(subjects, property)))
                            .collect(Collectors.toSet());
                    reversePredicates = OppModel.INSTANCE.listReversePredicates(subjects).stream()
                            .filter(property -> typeFilter.test(OppModel.INSTANCE.listSubjects(property, subjects)))
                            .collect(Collectors.toSet());
                }

                // add model options
                addModelTraverseLookupElements(forwardPredicates,
                        TraverseDirection.FORWARD,
                        availableNamespaces,
                        result);
                addModelTraverseLookupElements(reversePredicates,
                        TraverseDirection.REVERSE,
                        availableNamespaces,
                        result);

            }
        });
    }

    private void addModelTraverseLookupElements(Set<Property> predicates,
                                                TraverseDirection direction,
                                                Map<String, String> availableNamespaces,
                                                CompletionResultSet result) {
        predicates.stream()
                .map(resource -> TTLResourceUtil.getPredicateLookupElement(resource,
                        direction,
                        availableNamespaces))
                .filter(Objects::nonNull)
                .map(lookupElement -> PrioritizedLookupElement.withPriority(lookupElement, Traverse.getValue()))
                .forEach(result::addElement);
    }

    public static String parseToCurie(Resource resource,
                                      Map<String, String> availableNamespaces) {
        return Optional.ofNullable(resource.getURI())
                .map(uri -> getCurie(resource, availableNamespaces, uri))
                .orElse(null);
    }

    private static String getCurie(Resource resource,
                                   Map<String, String> availableNamespaces,
                                   String uri) {
        return availableNamespaces.containsKey(resource.getNameSpace()) ?
                (availableNamespaces.get(resource.getNameSpace()) + ":" + resource.getLocalName()) :
                "<" + uri + ">";
    }

}
