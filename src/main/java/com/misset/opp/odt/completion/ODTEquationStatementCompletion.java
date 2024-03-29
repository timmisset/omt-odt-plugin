package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.odt.psi.ODTEquationStatement;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryPath;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.intellij.patterns.StandardPatterns.and;
import static com.misset.opp.odt.completion.CompletionPatterns.COMPLETION_PRIORITY.ROOT_ELEMENT;
import static com.misset.opp.odt.completion.CompletionPatterns.FIRST_QUERY_STEP;
import static com.misset.opp.odt.completion.CompletionPatterns.INSIDE_EQUATION_STATEMENT;

public class ODTEquationStatementCompletion extends CompletionContributor {
    private static final ElementPattern<PsiElement> PATTERN = and(INSIDE_EQUATION_STATEMENT, FIRST_QUERY_STEP);

    public ODTEquationStatementCompletion() {
        // the position rules where a Traverse and QueryOperator can be suggested is identical
        extend(CompletionType.BASIC, PATTERN, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                final ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                final Map<String, String> availableNamespaces = originalFile.getAvailableNamespaces();

                PsiElement position = parameters.getPosition();

                ODTEquationStatement equationStatement = PsiTreeUtil.getParentOfType(position, ODTEquationStatement.class);
                if (equationStatement != null) {
                    String curiePrefixMatcher = ODTCompletionUtil.getCuriePrefixMatcher(parameters, result, ODTQueryPath.class);
                    result = result.withPrefixMatcher(curiePrefixMatcher);
                    addCompletionsForEquationStatement(result, availableNamespaces, position, equationStatement);
                }
            }

            private void addCompletionsForEquationStatement(@NotNull CompletionResultSet result,
                                                            Map<String, String> availableNamespaces,
                                                            PsiElement position,
                                                            ODTEquationStatement equationStatement) {
                List<ODTQuery> queryList = equationStatement.getQueryList();
                if (queryList.size() == 2 &&
                        PsiTreeUtil.getDeepestFirst(queryList.get(1)) == PsiTreeUtil.getDeepestFirst(position)) {
                    // only accept the right-side as completion, i.e.
                    // .. / rdf:type == <caret> should show the current class and all possible sub-classes
                    // or
                    // .. / ont:type == <caret> should show the type and all possible instances of that type
                    // resolve the left-side to check if this is a rdf:type check and what the known classes and subclasses are
                    ODTQuery odtQuery = queryList.get(0);
                    Set<OntResource> leftSideResolved = odtQuery.resolve();

                    addClasses(leftSideResolved, availableNamespaces, result, position.getProject());
                    addInstances(leftSideResolved, availableNamespaces, result, position.getProject());
                }
            }

            private void addClasses(Set<OntResource> leftSideResolved,
                                    Map<String, String> availableNamespaces,
                                    CompletionResultSet result,
                                    Project project) {
                Set<OntClass> classes = leftSideResolved.stream()
                        .filter(OntologyModel.getInstance(project)::isClass)
                        .map(OntologyModel.getInstance(project)::toClass)
                        .collect(Collectors.toCollection(HashSet::new));
                classes.addAll(OntologyModel.getInstance(project).listSubclasses(classes));
                classes.stream()
                        .map(resource -> OntologyResourceUtil.getInstance(project).getRootLookupElement(resource, "Class", availableNamespaces))
                        .filter(Objects::nonNull)
                        .map(lookupElement -> PrioritizedLookupElement.withPriority(lookupElement, ROOT_ELEMENT.getValue()))
                        .forEach(result::addElement);
            }

            private void addInstances(Set<OntResource> leftSideResolved,
                                      Map<String, String> availableNamespaces,
                                      CompletionResultSet result,
                                      Project project) {
                leftSideResolved.stream()
                        .filter(OntologyModel.getInstance(project)::isIndividual)
                        .map(Individual.class::cast)
                        .map(Individual::getOntClass)
                        .map(Resource::getURI)
                        .filter(Objects::nonNull)
                        .map(OntologyModel.getInstance(project)::toIndividuals)
                        .flatMap(Collection::stream)
                        .filter(this::isRealInstance)
                        .map(
                                resource -> OntologyResourceUtil
                                        .getInstance(project)
                                        .getRootLookupElement(resource, "Instance", availableNamespaces))
                        .filter(Objects::nonNull)
                        .map(lookupElement -> PrioritizedLookupElement.withPriority(lookupElement, ROOT_ELEMENT.getValue()))
                        .forEach(result::addElement);
            }

            private boolean isRealInstance(OntResource resource) {
                String uri = resource.getURI();
                return uri != null && !uri.endsWith("_INSTANCE");
            }
        });
    }
}
