package com.misset.opp.ttl.stubs;

import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import com.misset.opp.ttl.TTLLanguage;
import com.misset.opp.ttl.psi.TTLFile;
import org.jetbrains.annotations.NotNull;

public class TTLStubFileElementType extends IStubFileElementType<TTLFileStub> {
    public TTLStubFileElementType() {
        super("TTLStubFile", TTLLanguage.INSTANCE);
    }

    public static final int VERSION = 1;
    public static final TTLStubFileElementType INSTANCE = new TTLStubFileElementType();

    @Override
    public StubBuilder getBuilder() {
        return new DefaultStubBuilder() {
            @Override
            protected StubElement createStubForFile(@NotNull PsiFile file) {
                if (file instanceof TTLFile) {
                    return new TTLFileStub((TTLFile) file);
                }
                return super.createStubForFile(file);
            }
        };
    }

    @Override
    public int getStubVersion() {
        return VERSION;
    }

    @Override
    public void serialize(TTLFileStub stub, @NotNull StubOutputStream dataStream) {
        // not used
    }

    @NotNull
    @Override
    public TTLFileStub deserialize(@NotNull StubInputStream dataStream,
                                   StubElement parentStub) {
        return new TTLFileStub(null);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "TTL.FILE";
    }
}
