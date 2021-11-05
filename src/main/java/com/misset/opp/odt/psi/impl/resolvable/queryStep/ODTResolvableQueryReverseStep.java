package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableQueryReverseStep extends ODTResolvableQueryStep implements ODTQueryReverseStep {
    public ODTResolvableQueryReverseStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> calculate() {
        final ODTQueryStep queryStep = getQueryStep();
        if(queryStep instanceof ODTResolvableCurieElementStep) {
            // a reverse path indicator can only be applied to a curie step
            final Property predicate = ((ODTResolvableCurieElementStep) queryStep).getFullyQualified();
            return OppModel.INSTANCE.listSubjects(predicate, resolvePreviousStep());
        }
        return Collections.emptySet();
    }
}
