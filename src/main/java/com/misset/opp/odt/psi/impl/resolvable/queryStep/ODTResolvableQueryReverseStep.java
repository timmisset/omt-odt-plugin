package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * A backward traverse, or reverse, step in the query. The previous position is considered the object
 * and the model returns all subjects that point to this object with the specified predicate (the current position)
 */
public abstract class ODTResolvableQueryReverseStep extends ODTResolvableQueryStep implements ODTQueryReverseStep {
    public ODTResolvableQueryReverseStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        final ODTQueryStep queryStep = getQueryStep();
        // only when the reverse caret is followed by a valid traversion can path be travelled
        // - a curie => ont:somePredicate)
        // - an iri  => <http://ontology/somePredicate>
        if(queryStep instanceof ODTResolvableUriStep) {
            final OppModel oppModel = OppModel.INSTANCE;
            // a reverse path indicator can only be applied to a curie step
            final String fullyQualified = ((ODTResolvableUriStep) queryStep).getFullyQualifiedUri();
            return oppModel.listSubjects(oppModel.createProperty(fullyQualified), resolvePreviousStep());
        }
        return Collections.emptySet();
    }
}
