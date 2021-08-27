package com.misset.opp.omt;

import com.intellij.lang.ParserDefinition;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.omt.psi.impl.OMTFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLParserDefinition;

/**
 * The OMTParserDefinition will cast specific items from the YAML Language into dedicated OMT classes
 * Moreover, the returned filetype will be an OMTFileImpl class
 */
public class OMTParserDefinition extends YAMLParserDefinition implements ParserDefinition {

    private static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(OMTLanguage.INSTANCE);

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new OMTFileImpl(viewProvider);
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }
}
