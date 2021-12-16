package com.misset.opp.ttl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.ttl.psi.TTLFileImpl;
import com.misset.opp.ttl.psi.TTLIgnored;
import com.misset.opp.ttl.psi.TTLTypes;
import com.misset.opp.ttl.stubs.TTLStubFileElementType;
import org.intellij.sdk.language.parser.TTLParser;
import org.jetbrains.annotations.NotNull;

public class TTLParserDefinition implements com.intellij.lang.ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet IGNORED_COMMENTS = TokenSet.create(TTLIgnored.COMMENT);
    public static final TokenSet STRINGS = TokenSet.create(TTLTypes.STRING_LITERAL_LONG_QUOTE, TTLTypes.STRING_LITERAL_QUOTE, TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE, TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new TTLLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return IGNORED_COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new TTLParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return TTLStubFileElementType.INSTANCE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new TTLFileImpl(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return TTLTypes.Factory.createElement(node);
    }
}
