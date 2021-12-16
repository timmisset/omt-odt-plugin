package com.misset.opp.ttl.style;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.ttl.TTLLexerAdapter;
import com.misset.opp.ttl.psi.TTLIgnored;
import com.misset.opp.ttl.psi.TTLTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TTLSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("END_OF_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
    public static final TextAttributesKey SUBJECTS =
            createTextAttributesKey("SUBJECTS", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey PREDICATES =
            createTextAttributesKey("PREDICATES", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey OBJECTS =
            createTextAttributesKey("OBJECTS", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] PREDICATE_KEYS = new TextAttributesKey[]{PREDICATES};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private static final TokenSet STRING_TOKENS = TokenSet.create(
            TTLTypes.STRING,
            TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE,
            TTLTypes.STRING_LITERAL_QUOTE,
            TTLTypes.STRING_LITERAL_LONG_QUOTE,
            TTLTypes.STRING_LITERAL_SINGLE_QUOTE
    );

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new TTLLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType == TTLIgnored.COMMENT) {
            return COMMENT_KEYS;
        } else if (tokenType == TokenType.BAD_CHARACTER) {
            return BAD_CHAR_KEYS;
        } else if (STRING_TOKENS.contains(tokenType)) {
            return STRING_KEYS;
        } else if (tokenType == TTLTypes.A) {
            return PREDICATE_KEYS;
        }
        return EMPTY_KEYS;
    }

}
