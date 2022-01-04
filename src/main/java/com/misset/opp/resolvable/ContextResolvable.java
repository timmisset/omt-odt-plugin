package com.misset.opp.resolvable;

import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Extension of the Resolvable interface that provides a context-based resolve method
 * In general, when context information is available, this method should be preferred.
 */
public interface ContextResolvable extends Resolvable {
    /**
     * Resolve with inputResources and the Call
     * Returns an empty Set when no resolve type can be determined
     */
    default @NotNull Set<OntResource> resolve(Context context) {
        return resolve();
    }
}
