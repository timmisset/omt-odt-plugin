package com.misset.opp.callable;

import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Generic resolvable interface, not limited to PsiElements but also BuiltIn instances etc
 */
public interface Resolvable {

    @NotNull
    Set<OntResource> resolve();
    
}
