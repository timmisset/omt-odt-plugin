package com.misset.opp.ttl.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.misset.opp.ttl.psi.TTLSubject;
import org.jetbrains.annotations.NotNull;

public class TTLSubjectStubIndex extends StringStubIndexExtension<TTLSubject> {
    public static final StubIndexKey<String, TTLSubject> KEY = StubIndexKey.createIndexKey("SubjectStubIndex");

    @Override
    public @NotNull StubIndexKey<String, TTLSubject> getKey() {
        return KEY;
    }
}
