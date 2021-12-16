package com.misset.opp.ttl.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.TTLLanguage;
import org.jetbrains.annotations.NotNull;

public class TTLFileImpl extends PsiFileBase implements TTLFile {
    public TTLFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TTLLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return TTLFileType.INSTANCE;
    }

}
