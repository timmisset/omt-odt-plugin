package com.misset.opp.ttl.psi.extend;

import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nullable;

/**
 * Elements that are (part of) an iri/curie which can be resolved to a qualified iri.
 * Container interface to treat curies and iris in the same manner
 */
public interface TTLQualifiedIriResolver extends PsiNamedElement {
    /**
     * Returns the fully qualified IRI resource, without any wrapping
     */
    @Nullable String getQualifiedIri();

}
