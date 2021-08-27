package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.misset.opp.odt.ODTParserDefinition.ODTFileElementType;

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
