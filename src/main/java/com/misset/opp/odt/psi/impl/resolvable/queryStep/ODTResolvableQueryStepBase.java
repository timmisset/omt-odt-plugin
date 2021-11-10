package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.annotation.ODTAnnotatedElement;
import com.misset.opp.odt.inspection.ODTInspectedElement;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableQueryStepBase extends ASTWrapperPsiElement
        implements ODTQueryStep, ODTResolvable, ODTAnnotatedElement, ODTInspectedElement {
    public ODTResolvableQueryStepBase(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Method to calculate the ResourceSet for this QueryStep
     * Should be overridden by every implementation of ODTQueryStep
     */
    @Override
    public Set<OntResource> resolve() {
        return Collections.emptySet();
    }

    /**
     * Returns the resolve QueryOperation container of this step
     * If steps are further encapsulated, this method should be overridden to return the QueryOperation
     */
    public ODTResolvableQueryOperationStep getResolvableParent() {
        return PsiTreeUtil.getParentOfType(this, ODTResolvableQueryOperationStep.class);
    }

    protected boolean isRootStep() {
        return getResolvableParent().isRootStep();
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Optional.of(getResolvableParent())
                .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                .orElse(Collections.emptySet());
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        annotateResolved(holder);
    }
    @Override
    public void inspect(ProblemsHolder holder) {

    }

    protected void annotateResolved(AnnotationHolder holder) {
        final Set<OntResource> resources = getResolvableParent().resolve();
        if (resources == null) {
            return;
        }
        if (!resources.isEmpty()) {
            holder.newAnnotation(HighlightSeverity.INFORMATION, "")
                    .tooltip(
                            resources.stream()
                                    .sorted(Comparator.comparing(Resource::getURI))
                                    .map(this::describeUri)
                                    .collect(Collectors.joining("<br>")))
                    .create();
        }
    }
    protected void inspectResolved(ProblemsHolder holder) {
        if (resolve().isEmpty() && !resolvePreviousStep().isEmpty()) {
            final String fullyQualifiedUri = getFullyQualifiedUri();
            if (fullyQualifiedUri != null) {
                String direction = isPartOfReverseStep() ? "REVERSE" : "FORWARD";
                holder.registerProblem(this, "Could not traverse " + direction + " using predicate: " + fullyQualifiedUri);
            }
        }
    }
    protected String getFullyQualifiedUri() {
        return null;
    }
    protected boolean isPartOfReverseStep() {
        return PsiTreeUtil.getParentOfType(this, ODTQueryStep.class) instanceof ODTResolvableQueryReverseStep;
    }

    protected String describeUri(OntResource resource) {
        if (resource.isClass()) {
            if(resource.getNameSpace().equals(OppModel.XSD)) {
                return resource.getURI() + " (TYPE)";
            }
            return resource.getURI() + " (CLASS)";
        } else if (resource instanceof Individual) {
            final Individual individual = (Individual) resource;
            if (resource.getURI() == null) {
                return individual.getOntClass().getURI() + " (VALUE)";
            }
            return individual.getOntClass().getURI() + " (INSTANCE)";
        } else {
            return resource.getURI();
        }
    }
}
