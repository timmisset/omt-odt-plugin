package com.misset.opp.ttl.stubs.object;

import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLObject;

public interface TTLObjectStub extends StubElement<TTLObject> {

    String getQualifiedUri();

    boolean isPredicate();

    boolean isObject();

    String getSubjectIri();
}
