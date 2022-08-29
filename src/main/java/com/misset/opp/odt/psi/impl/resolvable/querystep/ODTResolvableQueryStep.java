package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTResolvableQueryStep extends ODTResolvable {
    Set<OntResource> resolvePreviousStep();
}
