package com.misset.opp.ttl.stubs.object;

import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLStubBasedObject;

public interface TTLObjectStub extends StubElement<TTLStubBasedObject> {

    String getQualifiedUri();

    boolean isPredicate();

    boolean isObject();

    String getSubjectIri();
}
