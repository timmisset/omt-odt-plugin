package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTFile;
import org.jetbrains.annotations.NotNull;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    public ODTFileImpl(@NotNull FileViewProvider provider) {
        super(provider, ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ODTFileType.INSTANCE;
    }
}
