package com.misset.opp.odt.psi.resolvable.query;

import com.intellij.openapi.util.Pair;
import com.misset.opp.odt.psi.ODTQueryStep;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.Set;

public interface ODTResolvableQueryPath extends ODTResolvableQuery {

    Set<OntResource> resolveFromSet(Set<OntResource> fromSet);

    Set<OntResource> getFromSet();

    /**
     * Returns the QueryStep by reversed index:
     * /ont:ClassA / ont:property / ont:anotherProperty
     * index = 0 => ont:anotherProperty
     * index = 1 => ont:property
     * etc...
     */
    ODTQueryStep getStepMovingBackward(int index);

    /**
     * Tries to split the query into a subject part (last-1 step) and a predicate (last step)
     * /ont:ClassA / ^rdf:type / ont:property;
     * subject = ont:ClassA_INSTANCE
     * predicate = ont:property
     */
    Pair<Set<OntResource>, Property> resolveToSubjectPredicate();

    /**
     * Returns true if this QueryPath starts the root indicator: /
     * /ont:ClassA => true
     * ont:property / ont:anotherProperty => false
     * $variable / ont:property => false
     */
    boolean isAbsolutePath();
}
