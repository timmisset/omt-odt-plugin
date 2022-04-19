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
                HashMap<Property, Set<OntResource>> forwardPredicates = new HashMap<>();
                HashMap<Property, Set<OntResource>> reversePredicates = new HashMap<>();
                if (subjects.isEmpty()) {
                    // cannot establish subject, only show the rdf:type predicates
                    forwardPredicates.put(OppModel.INSTANCE.RDF_TYPE, Collections.emptySet());
                    reversePredicates.put(OppModel.INSTANCE.RDF_TYPE, Collections.emptySet());
                } else {
                    OppModel.INSTANCE.listPredicates(subjects)
                            .forEach(property -> {
                                Set<OntResource> predicateObjects = OppModel.INSTANCE.listObjects(subjects, property);
                                if (typeFilter.test(predicateObjects)) {
                                    forwardPredicates.put(property, predicateObjects);
                                }
                            });
                    OppModel.INSTANCE.listReversePredicates(subjects)
                            .forEach(property -> {
                                Set<OntResource> predicateSubjects = OppModel.INSTANCE.listSubjects(property, subjects);
                                if (typeFilter.test(predicateSubjects)) {
                                    reversePredicates.put(property, predicateSubjects);
                                }
                            });
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

    private void addModelTraverseLookupElements(HashMap<Property, Set<OntResource>> predicateObjects,
                                                TraverseDirection direction,
                                                Map<String, String> availableNamespaces,
                                                CompletionResultSet result) {
        predicateObjects.keySet()
                .stream()
                .map(predicate -> TTLResourceUtil.getPredicateLookupElement(
                        predicate,
                        predicateObjects.get(predicate),
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
