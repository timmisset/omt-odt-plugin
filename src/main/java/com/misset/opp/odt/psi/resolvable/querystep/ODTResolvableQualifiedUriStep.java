package com.misset.opp.odt.psi.resolvable.querystep;

import com.intellij.openapi.util.TextRange;

public interface ODTResolvableQualifiedUriStep extends ODTResolvableQueryStep {

    String getNamespace();

    String getLocalName();

    String getFullyQualifiedUri();

    /**
     * Return the TextRange that should be used for the reference to the TTL model
     */
    TextRange getModelReferenceTextRange();

}
