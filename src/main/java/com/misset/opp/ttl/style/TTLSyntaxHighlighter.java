package com.misset.opp.ttl.style;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.TTLLexerAdapter;
import com.misset.opp.ttl.psi.TTLIgnored;
import com.misset.opp.ttl.psi.TTLTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TTLSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final HashMap<IElementType, TextAttributesKey> highlightingMap = new HashMap<>();

    private static final HashMap<String, TextAttributesKey> attributeKeys = new HashMap<>();

    static {
        // literals:
        addToMap(new IElementType[]{
                TTLTypes.STRING,
                TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE,
                TTLTypes.STRING_LITERAL_QUOTE,
                TTLTypes.STRING_LITERAL_LONG_QUOTE,
                TTLTypes.STRING_LITERAL_SINGLE_QUOTE
        }, "TTL_Literals//String", DefaultLanguageHighlighterColors.STRING);
        addToMap(new IElementType[]{
                TTLTypes.TRUE, TTLTypes.FALSE
        }, "TTL_Literals//Boolean", DefaultLanguageHighlighterColors.CONSTANT);
        addToMap(new IElementType[]{
                TTLTypes.INTEGER, TTLTypes.DOUBLE, TTLTypes.DECIMAL
        }, "TTL_Literals//Number", DefaultLanguageHighlighterColors.NUMBER);

        // keywords:
        addToMap(TTLTypes.A, "TTL_Keywords//A", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(TTLTypes.ATPREFIX, "TTL_Keywords//AT_PREFIX", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(TTLTypes.PREFIX_LEADING, "TTL_Keywords//PREFIX", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(TTLTypes.ATBASE, "TTL_Keywords//AT_BASE", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(TTLTypes.BASE_LEADING, "TTL_Keywords//BASE", DefaultLanguageHighlighterColors.KEYWORD);

        // parenthesis:
        addToMap(new IElementType[]{TTLTypes.PARENTHESES_OPEN, TTLTypes.PARENTHESES_CLOSE}, "TTL_Braces and Operators//Parentheses", DefaultLanguageHighlighterColors.PARENTHESES);
        addToMap(new IElementType[]{TTLTypes.BRACKET_OPEN, TTLTypes.BRACKET_CLOSE}, "TTL_Braces and Operators//Brackets", DefaultLanguageHighlighterColors.BRACKETS);
        addToMap(TTLTypes.SEMICOLON, "TTL_Braces and Operators//Semicolon", DefaultLanguageHighlighterColors.SEMICOLON);
        addToMap(TTLTypes.DOT, "TTL_Braces and Operators//Dot", DefaultLanguageHighlighterColors.DOT);
        addToMap(TTLTypes.COMMA, "TTL_Braces and Operators//Comma", DefaultLanguageHighlighterColors.COMMA);
        addToMap(TTLTypes.DATATYPE_LEADING, "TTL_Braces and Operators//Datatype leading", DefaultLanguageHighlighterColors.MARKUP_TAG);

        // comments:
        addToMap(TTLIgnored.COMMENT, "TTL_Comments//Line comment", DefaultLanguageHighlighterColors.LINE_COMMENT);
        addToMap(TTLTypes.BASE_URI, "TTL_Comments//Base uri", DefaultLanguageHighlighterColors.LINE_COMMENT);
        addToMap(TTLTypes.IMPORT_URI, "TTL_Comments//Import uri", DefaultLanguageHighlighterColors.LINE_COMMENT);

        // identifiers
        addToMap(TTLTypes.PNAME_NS, "TTL_Identifiers//Prefix", DefaultLanguageHighlighterColors.IDENTIFIER);
        addToMap(TTLTypes.PN_LOCAL, "TTL_Identifiers//Localname", DefaultLanguageHighlighterColors.IDENTIFIER);
        addToMap(TTLTypes.IRIREF, "TTL_Identifiers//IRI", DefaultLanguageHighlighterColors.IDENTIFIER);

    }

    public static Collection<TextAttributesKey> getAttributes() {
        return new ArrayList<>(attributeKeys.values());
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new TTLLexerAdapter();
    }

    private static void addToMap(IElementType type, String name, TextAttributesKey fallback) {
        TextAttributesKey textAttributesKey = attributeKeys.computeIfAbsent(name, s -> createTextAttributesKey(name, fallback));
        highlightingMap.put(type, textAttributesKey);
    }

    private static void addToMap(IElementType[] types, String name, TextAttributesKey fallback) {
        for (IElementType type : types) {
            addToMap(type, name, fallback);
        }
    }

    @NotNull
    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return Optional.ofNullable(highlightingMap.get(tokenType))
                .map(textAttributesKey -> new TextAttributesKey[]{textAttributesKey})
                .orElse(TextAttributesKey.EMPTY_ARRAY);
    }
}
