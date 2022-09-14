package com.misset.opp.odt;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.exception.OMTODTPluginException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTIgnored;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import org.intellij.sdk.language.parser.ODTParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ODTParserDefinition implements ParserDefinition {
    public static final IFileElementType ODTFileElementType = new IFileElementType(ODTLanguage.INSTANCE);
    private static final TokenSet COMMENTS = TokenSet.create(
            ODTIgnored.END_OF_LINE_COMMENT, ODTIgnored.MULTILINE, ODTIgnored.DOC_COMMENT_START
    );

    private static Function<FileViewProvider, ODTFile> fileViewProvider2ODTFile = ODTFileImpl::new;

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new ODTLexerAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new ODTParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return ODTFileElementType;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return ODTTypes.Factory.createElement(node);
    }

    public static void setFileViewProvider2ODTFile(Function<FileViewProvider, ODTFile> fileViewProvider2ODTFile) {
        if (ApplicationManager.getApplication() != null && !ApplicationManager.getApplication().isUnitTestMode()) {
            throw new OMTODTPluginException("The FileViewProvider2ODTFile parsing mechnanism should not be changed at runtime." +
                    "It is only meant to provide an extended ODTFile during unit-testing which can more easily be configured to" +
                    "test certain behavior");
        }
        ODTParserDefinition.fileViewProvider2ODTFile = fileViewProvider2ODTFile;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return fileViewProvider2ODTFile.apply(viewProvider);
    }
}
