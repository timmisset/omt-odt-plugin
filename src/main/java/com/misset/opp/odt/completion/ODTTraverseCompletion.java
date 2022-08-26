package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryOperationStep;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
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
    private static final Key<Boolean> HAS_CARET = new Key<>("HAS_CARET");
    private static final Key<Boolean> HAS_PREFIX = new Key<>("HAS_PREFIX");
    ElementPattern<PsiElement> TRAVERSE =
            or(AFTER_FIRST_QUERY_STEP, INSIDE_DEFINED_QUERY, INSIDE_QUERY_FILTER);

    public enum TraverseDirection {
        FORWARD, REVERSE
    }

    public static boolean isForward(TraverseDirection traverseDirection) {
        return traverseDirection == TraverseDirection.FORWARD;
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
                context.put(HAS_PREFIX, position.getPrevSibling() instanceof ODTNamespacePrefix);
                context.put(HAS_CARET, PsiTreeUtil.getParentOfType(position.getPrevSibling(), ODTQueryReverseStep.class, true, ODTQuery.class) != null);
                final Set<OntResource> subjects = Optional.of(position)
                        .map(element -> PsiTreeUtil.getParentOfType(element, ODTResolvableQueryOperationStep.class))
                        .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                        .orElse(Collections.emptySet());
                HashMap<Property, Set<OntResource>> forwardPredicates = new HashMap<>();
                HashMap<Property, Set<OntResource>> reversePredicates = new HashMap<>();
                if (subjects.isEmpty()) {
                    // cannot establish subject, only show the rdf:type predicates
                    forwardPredicates.put(OppModelConstants.RDF_TYPE, Collections.emptySet());
                    reversePredicates.put(OppModelConstants.RDF_TYPE, Collections.emptySet());
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
                addModelTraverseLookupElements(
                        subjects,
                        forwardPredicates,
                        TraverseDirection.FORWARD,
                        availableNamespaces,
                        result,
                        context);
                addModelTraverseLookupElements(
                        subjects,
                        reversePredicates,
                        TraverseDirection.REVERSE,
                        availableNamespaces,
                        result,
                        context);

            }
        });
    }

    private void addModelTraverseLookupElements(
            Set<OntResource> subjects,
            HashMap<Property, Set<OntResource>> predicateObjects,
            TraverseDirection direction,
            Map<String, String> availableNamespaces,
            CompletionResultSet result,
            ProcessingContext context) {
        String prefixMatcher = result.getPrefixMatcher().getPrefix();
        predicateObjects.keySet()
                .forEach(predicate -> {
                    String curie = ODTTraverseCompletion.parseToCurie(predicate, availableNamespaces);
                    if (!isForward(direction)) {
                        curie = "^" + curie;
                    }
                    String prefix = curie.contains(":") ? curie.substring(0, curie.indexOf(":") + 1) : null;

                    LookupElementBuilder predicateLookupElement = TTLResourceUtil.getPredicateLookupElement(
                            subjects,
                            predicate,
                            predicateObjects.get(predicate),
                            direction,
                            curie);

                    LookupElement withPriority = PrioritizedLookupElement.withPriority(predicateLookupElement, Traverse.getValue());
                    String curieCompletion = prefix + prefixMatcher; // i.e. ont:cla<caret>
                    if (curieCompletion.startsWith("^") && !context.get(HAS_CARET)) {
                        curieCompletion = curieCompletion.substring(1);
                    }

                    if (prefix != null && context.get(HAS_PREFIX) && curie.contains(curieCompletion)) {
                        result.withPrefixMatcher(curieCompletion).addElement(withPriority);
                    } else {
                        result.addElement(withPriority);
                    }
                });
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
