package com.misset.opp.ttl.stubs.subject;

import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLSubject;

public interface TTLSubjectStub extends StubElement<TTLSubject> {

    String getQualifiedUri();

}
