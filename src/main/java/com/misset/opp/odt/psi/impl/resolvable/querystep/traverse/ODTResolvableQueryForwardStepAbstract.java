package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.intellij.lang.ASTNode;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQualifiedUriStepAbstract;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Forward model traversion
 * Requires a fully qualified URI as predicate to traverse from the previous step (subject) into the next (object)
 */
public abstract class ODTResolvableQueryForwardStepAbstract extends ODTResolvableQualifiedUriStepAbstract {
    protected ODTResolvableQueryForwardStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final String fullyQualifiedUri = getFullyQualifiedUri();
        if (fullyQualifiedUri == null) {
            return Collections.emptySet();
        }

        final OntologyModel model = OntologyModel.getInstance(getProject());
        if (isRootStep()) {
            // when the path start with a root curie, resolve the curie and return it:
            final OntResource resource = model.getOntResource(fullyQualifiedUri, getProject());
            return resource != null ? Set.of(resource) : Collections.emptySet();
        } else {
            // resolve the previous step and use the current curie to traverse the model
            final Property property = model.getProperty(fullyQualifiedUri);
            if (property == null) {
                return Collections.emptySet();
            }
            return model.appendInstancesWithSubclasses(model.listObjects(resolvePreviousStep(), property));
        }
    }

}
