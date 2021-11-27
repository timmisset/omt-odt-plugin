package com.misset.opp.callable;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Generic resolvable interface, not limited to PsiElements but also BuiltIn instances etc
 */
public interface Resolvable {

    /**
     * A context-less way of trying to resolve the Resolvable
     * Used to determine types within, for example, a query where there is no context available at the point
     * of the PsiElement that is being resolved.
     * <p>
     * Example:
     * /ont:ClassA / ^rdf:type => resolves to http://ontology#ClassA_INSTANCE
     */
    @NotNull
    Set<OntResource> resolve();

    /**
     * Resolve with context
     * The type of input can be used to determine the output. For example when a call is made from one query to another:
     * <p>
     * /ont:ClassA / ^rdf:type / someCall;
     * someCall is called with resolve(<http://ontology#ClassA_INSTANCE>)
     */
    default Set<OntResource> resolve(Set<OntResource> resources) {
        return resolve();
    }

    /**
     * Resolve with context
     * The type of input and information about the call can be used to determine the output.
     * <p>
     * For blackbox Builtin commands and operators this information is not used to determine the outcome but it
     * can be used to determine the validity of the call
     * For resolvable PsiElements, such as other queries, the information is passed to determine
     * the outcome for the specific call
     */
    default Set<OntResource> resolve(Set<OntResource> resources,
                                     Call call) {
        return resolve(resources);
    }

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
