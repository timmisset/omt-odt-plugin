package com.misset.opp.ttl.psi.spo;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.StubElement;

public interface TTLSPO<T extends StubElement<? extends PsiElement>> extends StubBasedPsiElement<T> {

    String getQualifiedUri();

    default boolean isSubject() {
        return false;
    }

    default boolean isPredicate() {
        return false;
    }

    default boolean isObjectClass() {
        return false;
    }

    String getSubjectIri();

}
