package com.misset.opp.ttl.psi.extend;

import com.intellij.psi.PsiElement;

/**
 * Additional methods that should be implemented available via the generated interface
 * <p>
 * Don't use this extended interface, use the generated interface instead
 *
 * @see com.misset.opp.ttl.psi.TTLDeclarePrefix
 */
public interface TTLDeclarePrefix extends PsiElement {

    /**
     * Returns the qualified Iri based on the declared namespace for this prefix
     * and the provided localname to be appended to it.
     */
    String getQualifiedIri(String localname);

}
