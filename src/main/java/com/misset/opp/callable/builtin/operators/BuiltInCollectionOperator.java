package com.misset.opp.callable.builtin.operators;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

/**
 * Operator that returns that same type as the input
 */
public abstract class BuiltInCollectionOperator extends BuiltInOperator {

    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources;
    }
}
