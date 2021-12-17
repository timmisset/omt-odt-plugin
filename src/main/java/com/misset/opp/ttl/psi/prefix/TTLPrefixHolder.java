package com.misset.opp.ttl.psi.prefix;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiNamedElement;

public interface TTLPrefixHolder extends PsiNamedElement {

    String getPrefixId();

    TextRange getPrefixIdTextRange();

    String getIri();

}
