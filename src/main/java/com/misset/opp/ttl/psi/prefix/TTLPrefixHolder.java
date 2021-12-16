package com.misset.opp.ttl.psi.prefix;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

public interface TTLPrefixHolder extends PsiElement {

    String getPrefixId();

    TextRange getPrefixIdTextRange();

    String getIri();

}
