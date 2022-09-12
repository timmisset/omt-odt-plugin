package com.misset.opp.odt.psi.resolvable.callable;

import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface ODTResolvableDefineQueryStatement extends ODTDefineStatement {

    @NotNull Set<OntResource> getBase();

}
