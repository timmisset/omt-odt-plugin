package com.misset.opp.ttl.stubs.object;

import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLSubject;

public interface TTLObjectStub extends StubElement<TTLSubject> {

    String getQualifiedUri();

    boolean isPredicate();

    boolean isObject();

    String getSubjectIri();
}
