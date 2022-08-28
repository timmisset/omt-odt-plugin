package com.misset.opp.omt.injection;

import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.misset.opp.odt.ODTParserDefinition;
import org.jetbrains.annotations.NotNull;

public class OMTODTFragmentParserDefinition extends ODTParserDefinition {

    public static final IFileElementType OMTODTFileElementType = new IFileElementType(OMTODTFragmentLanguage.INSTANCE);

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new OMTODTFragment(viewProvider);
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return OMTODTFileElementType;
    }
}
