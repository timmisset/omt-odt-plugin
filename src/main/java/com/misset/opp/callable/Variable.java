package com.misset.opp.callable;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface Variable {

    String getName();

    String getDescription();

    Set<OntResource> getType();

    default boolean isParameter() {
        return false;
    }

    default boolean isReadonly() {
        return false;
    }

    default boolean isGlobal() {
        return false;
    }

}
