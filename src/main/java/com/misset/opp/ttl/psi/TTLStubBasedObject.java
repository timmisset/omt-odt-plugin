package com.misset.opp.ttl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import com.misset.opp.ttl.stubs.object.TTLObjectStub;

/**
 * In a predicateObjectList
 */
public interface TTLStubBasedObject extends StubBasedPsiElement<TTLObjectStub>, TTLQualifiedIriResolver {

    /**
     * True if the iri of the object describes a known predicate
     */
    boolean isPredicate();

    /**
     * True if the iri of the object describes a class. This is determined
     * by checking the predicateObject this Object is a part of. If the predicate
     * is one of:
     * - rdf:type
     * - rdfs:subClassOf
     * - sh:class
     */
    boolean isClass();

    /**
     * Returns the subjectIri for this Object
     */
    String getSubjectIri();

}
