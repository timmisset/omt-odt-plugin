package com.misset.opp.resolvable;

import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
     * Resolves to a literal value
     * Example:
     * 'test' resolves to "'test'^^xsd:string";
     */
    @NotNull List<Literal> resolveLiteral();

}
