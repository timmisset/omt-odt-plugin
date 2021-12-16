package com.misset.opp.ttl.stubs.object;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.misset.opp.ttl.TTLLanguage;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.impl.TTLObjectImpl;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TTLObjectStubElementType extends IStubElementType<TTLObjectStub, TTLObject> {
    public TTLObjectStubElementType(String debugName) {
        super(debugName, TTLLanguage.INSTANCE);
    }

    @Override
    public TTLObject createPsi(@NotNull TTLObjectStub stub) {
        return new TTLObjectImpl(stub, this);
    }

    @Override
    public @NotNull TTLObjectStub createStub(@NotNull TTLObject psi, StubElement<? extends PsiElement> parentStub) {
        return new TTLObjectStubImpl(parentStub,
                psi.getQualifiedUri(),
                psi.isPredicate(),
                psi.isObject(),
                psi.getSubjectIri());
    }

    @Override
    public @NotNull String getExternalId() {
        return "TTL.Object";
    }

    @Override
    public void serialize(@NotNull TTLObjectStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("iri", stub.getQualifiedUri());
        jsonObject.addProperty("isPredicate", stub.isPredicate());
        jsonObject.addProperty("isObject", stub.isObject());
        jsonObject.addProperty("subjectIri", stub.getSubjectIri());
        dataStream.writeName(jsonObject.getAsString());
    }

    @Override
    public @NotNull TTLObjectStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        String s = dataStream.readNameString();
        if (s == null) {
            throw new RuntimeException("Could not find stub information");
        }
        JsonObject json = JsonParser.parseString(s).getAsJsonObject();
        return new TTLObjectStubImpl(parentStub,
                json.get("iri").getAsString(),
                json.get("isPredicate").getAsBoolean(),
                json.get("isObject").getAsBoolean(),
                json.get("subjectIri").getAsString());
    }

    @Override
    public void indexStub(@NotNull TTLObjectStub stub, @NotNull IndexSink sink) {
        String qualifiedUri = stub.getQualifiedUri();
        if (qualifiedUri == null) {
            return;
        }
        sink.occurrence(TTLSubjectStubIndex.KEY, qualifiedUri);
    }
}
