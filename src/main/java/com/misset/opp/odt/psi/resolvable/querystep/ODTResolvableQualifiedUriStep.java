package com.misset.opp.odt.psi.resolvable.querystep;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiNamedElement;

public interface ODTResolvableQualifiedUriStep extends ODTResolvableQueryStep, PsiNamedElement {

    String getNamespace();

    String getLocalName();

    String getFullyQualifiedUri();

    /**
     * Return the TextRange that should be used for the reference to the TTL model
     */
    TextRange getModelReferenceTextRange();

}
