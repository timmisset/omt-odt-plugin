package com.misset.opp.callable;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface Variable {

    String getName();

    String getDescription();

    Set<OntResource> getType();

}
