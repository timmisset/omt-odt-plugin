package com.misset.opp.resolvable;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Anything that can be resolved to a Set of OntResource types.
 */
public interface Resolvable {

    /**
     * A context-less way of trying to resolve the Resolvable
     * Used to determine types within, for example, a query where there is no context available at the point
     * of the PsiElement that is being resolved.
     * <p>
     * Example:
     * /ont:ClassA / ^rdf:type => resolves to http://ontology#ClassA_INSTANCE
     * <p>
     * Returns an empty Set when no resolve type can be determined
     */
    @NotNull
    Set<OntResource> resolve();

    /**
     * Returns true if resolve contains a single value which is XSD_BOOLEAN_INSTANCE
     */
    default boolean isBoolean() {
        final Set<OntResource> resolved = resolve();
        return resolved.size() == 1 && resolved.contains(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    /**
     * Returns true if, and only if, it is absolutely certain that the Resolvable will contain multiple values
     * For example, an Array query
     */
    default boolean isMultiple() {
        return false;
    }
}
