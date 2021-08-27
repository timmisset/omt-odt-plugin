package com.misset.opp.odt;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.psi.ODTIgnored;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.odt.psi.wrapping.ODTWrapping;
import org.intellij.sdk.language.parser.ODTParser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTParserDefinition implements ParserDefinition {
    public static final IFileElementType ODTFileElementType = new IFileElementType(ODTLanguage.INSTANCE);
    private static final TokenSet COMMENTS = TokenSet.create(
            ODTIgnored.END_OF_LINE_COMMENT, ODTIgnored.JAVADOCS, ODTIgnored.MULTILINE
    );

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
        return Optional.ofNullable(ODTWrapping.createElement(node))
                .orElse(ODTTypes.Factory.createElement(node));
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new ODTFileImpl(viewProvider);
    }
}
