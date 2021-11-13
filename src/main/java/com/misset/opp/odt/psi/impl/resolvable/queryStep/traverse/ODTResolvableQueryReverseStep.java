package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryStepBase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * A backward traverse, or reverse, step in the query. The previous position is considered the object
 * and the model returns all subjects that point to this object with the specified predicate (the current position)
 */
public abstract class ODTResolvableQueryReverseStep extends ODTResolvableQueryStepBase implements ODTQueryReverseStep {
    public ODTResolvableQueryReverseStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final ODTQueryStep queryStep = getQueryStep();
        // only when the reverse caret is followed by a valid traversion can path be travelled
        // - a curie => ont:somePredicate)
        // - an iri  => <http://ontology/somePredicate>
        if(queryStep instanceof ODTResolvableQualifiedUriStep) {
            final OppModel oppModel = OppModel.INSTANCE;
            // a reverse path indicator can only be applied to a curie step
            final String fullyQualified = ((ODTResolvableQualifiedUriStep) queryStep).getFullyQualifiedUri();
            final Property property = oppModel.getProperty(fullyQualified);
            if(property == null) { return Collections.emptySet(); }
            return oppModel.listSubjects(property, resolvePreviousStep());
        }
        return Collections.emptySet();
    }

    @Override
    protected PsiElement getAnnotationRange() {
        if(getQueryStep() instanceof ODTResolvableCurieElementStep) {
            return ((ODTResolvableCurieElementStep) getQueryStep()).getAnnotationRange();
        }
        return super.getAnnotationRange();
    }
}