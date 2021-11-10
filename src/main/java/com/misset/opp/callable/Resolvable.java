package com.misset.opp.callable;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

/**
 * Generic resolvable interface, not limited to PsiElements but also BuiltIn instances etc
 */
public interface Resolvable {

    Set<OntResource> resolve();

}
