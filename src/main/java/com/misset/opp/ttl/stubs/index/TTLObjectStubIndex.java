package com.misset.opp.ttl.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.misset.opp.ttl.psi.TTLObject;
import org.jetbrains.annotations.NotNull;

public class TTLObjectStubIndex extends StringStubIndexExtension<TTLObject> {
    public static final StubIndexKey<String, TTLObject> KEY = StubIndexKey.createIndexKey("ObjectStubIndex");

    @Override
    public @NotNull StubIndexKey<String, TTLObject> getKey() {
        return KEY;
    }
}
