package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryOperationStep;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.util.Icons;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public class ODTTraverseCompletion extends CompletionContributor {

    private enum TraverseDirection {
        FORWARD, REVERSE
    }

    public ODTTraverseCompletion() {
        extend(CompletionType.BASIC, psiElement().inside(ODTQueryStep.class), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                final ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                final Map<String, String> availableNamespaces = originalFile.getAvailableNamespaces();

                final Set<OntResource> subjects = Optional.of(parameters.getPosition())
                        .map(element -> PsiTreeUtil.getParentOfType(element, ODTResolvableQueryOperationStep.class))
                        .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                        .orElse(Collections.emptySet());

                // add model options
                addModelTraverseLookupElements(OppModel.INSTANCE.listPredicates(subjects),
                        TraverseDirection.FORWARD,
                        availableNamespaces,
                        result);
                addModelTraverseLookupElements(OppModel.INSTANCE.listReversePredicates(subjects),
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
                .map(resource -> getPredicateLookupElement(resource,
                        direction,
                        availableNamespaces))
                .filter(Objects::nonNull)
                .forEach(result::addElement);
    }

    private LookupElement getPredicateLookupElement(Property resource,
                                                    TraverseDirection direction,
                                                    Map<String, String> availableNamespaces) {
        String title = parseToCurie(resource, availableNamespaces);
        if (title == null) {
            return null;
        }
        String lookupText = direction == TraverseDirection.REVERSE ? "^" + title : title;
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTailText(direction == TraverseDirection.FORWARD ? " -> forward" : " <- reverse")
                .withTypeText("", Icons.TTLFile, false)
                .withPresentableText(title);
    }

    private String parseToCurie(Resource resource,
                                Map<String, String> availableNamespaces) {
        final String uri = resource.getURI();
        if (uri == null) {
            return null;
        }
        return availableNamespaces.containsKey(resource.getNameSpace()) ?
                (availableNamespaces.get(resource.getNameSpace()) + ":" + resource.getLocalName()) :
                "<" + resource.getURI() + ">";
    }

}
