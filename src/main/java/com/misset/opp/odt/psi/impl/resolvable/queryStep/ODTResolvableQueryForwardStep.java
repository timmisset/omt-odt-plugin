package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Forward model traversion
 * Requires a fully qualified URI as predicate to traverse from the previous step (subject) into the next (object)
 */
public abstract class ODTResolvableQueryForwardStep extends ODTResolvableUriStep {

    public ODTResolvableQueryForwardStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        final String fullyQualifiedUri = getFullyQualifiedUri();
        if(fullyQualifiedUri == null) { return Collections.emptySet(); }

        final OppModel model = OppModel.INSTANCE;
        if (isRootStep()) {
            // when the path start with a root curie, resolve the curie and return it:
            final OntClass aClass = model.getClass(fullyQualifiedUri);
            return aClass != null ? Set.of(aClass) : Collections.emptySet();
        } else {
            // resolve the previous step and use the current curie to traverse the model
            final Property property = model.getProperty(fullyQualifiedUri);
            return property != null ? model.listObjects(resolvePreviousStep(), property) : Collections.emptySet();
        }
    }
}
