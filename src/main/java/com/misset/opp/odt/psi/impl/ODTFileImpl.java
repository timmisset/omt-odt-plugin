package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTLanguage;
import org.jetbrains.annotations.NotNull;

public class ODTFileImpl extends PsiFileBase {
    public ODTFileImpl(@NotNull FileViewProvider provider) {
        super(provider, ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ODTFileType.INSTANCE;
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
    }
}
