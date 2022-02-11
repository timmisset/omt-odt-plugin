package com.misset.opp.odt;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTIgnored;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.syntax.ODTHighlightingTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * SyntaxHighlighter works on the lexer output, this means that only the leaf AST nodes are queried.
 * For performance, a separate highlighting lexer is used which is only used for color coding
 */
public class ODTSyntaxHighlighter implements SyntaxHighlighter {
    public static final TextAttributesKey Variable = createTextAttributesKey("ODT_Variables & Parameters//Variable",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey GlobalVariable = createTextAttributesKey("ODT_Variables & Parameters//Global variable",
            Variable);
    public static final TextAttributesKey ReadonlyVariable = createTextAttributesKey("ODT_Variables & Parameters//Readonly variable",
            Variable);
    public static final TextAttributesKey Parameter = createTextAttributesKey("ODT_Variables & Parameters//Parameter",
            Variable);
    public static final TextAttributesKey Braces = createTextAttributesKey("ODT_Braces and Operators//Braces",
            DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey Brackets = createTextAttributesKey("ODT_Braces and Operators//Brackets",
            DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey Parentheses = createTextAttributesKey("ODT_Braces and Operators//Parentheses",
            DefaultLanguageHighlighterColors.PARENTHESES);

    private static final HashMap<IElementType, TextAttributesKey> highlightingMap = new HashMap<>();
    private static final HashMap<IElementType, TextAttributesKey> nonDisplayedHighlightingMap = new HashMap<>();

    // the ODT_ prefix is added to prevent any external name identifier conflicts with other languages
    static {
        addToMap(ODTTypes.STRING, "ODT_Literals//String", DefaultLanguageHighlighterColors.STRING);
        addToMap(ODTTypes.BOOLEAN, "ODT_Literals//Boolean", DefaultLanguageHighlighterColors.CONSTANT);
        addToMap(ODTTypes.NULL, "ODT_Literals//Null", DefaultLanguageHighlighterColors.CONSTANT);
        addToMap(ODTHighlightingTypes.OPERATOR_SIGN, "ODT_Braces and Operators//Operator Sign", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addToMap(ODTHighlightingTypes.KEYWORD, "ODT_Keyword", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(ODTTypes.TAG, "ODT_Flag", DefaultLanguageHighlighterColors.METADATA);
        addToMap(ODTHighlightingTypes.OPERATOR_CALLNAME, "ODT_Operator Call", DefaultLanguageHighlighterColors.FUNCTION_CALL);
        addToMap(ODTHighlightingTypes.COMMAND_CALLNAME, "ODT_Command Call", DefaultLanguageHighlighterColors.FUNCTION_CALL);
        addToMap(ODTTypes.SEMICOLON, "ODT_Braces and Operators//Semicolon", DefaultLanguageHighlighterColors.SEMICOLON);
        addToMap(ODTTypes.COMMA, "ODT_Braces and Operators//Comma", DefaultLanguageHighlighterColors.COMMA);
        addToMap(ODTTypes.LAMBDA, "ODT_Braces and Operators//Lambda", DefaultLanguageHighlighterColors.OPERATION_SIGN);
        addToMap(ODTIgnored.MULTILINE, "ODT_Comments//Block", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addToMap(JavaDocElementType.DOC_COMMENT, "ODT_Comments//Doc", DefaultLanguageHighlighterColors.DOC_COMMENT);
        addToMap(ODTIgnored.END_OF_LINE_COMMENT, "ODT_Comments//Line", DefaultLanguageHighlighterColors.LINE_COMMENT);
        addToMap(ODTHighlightingTypes.DEFINE_NAME, "ODT_Define name", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
        addToMap(ODTHighlightingTypes.NUMBER, "ODT_Literals//Number", DefaultLanguageHighlighterColors.NUMBER);
        addToMap(ODTHighlightingTypes.CURIE_PREFIX, "ODT_Curie//Prefix", DefaultLanguageHighlighterColors.IDENTIFIER);
        addToMap(ODTHighlightingTypes.CURIE_LOCALNAME, "ODT_Curie//Localname", DefaultLanguageHighlighterColors.IDENTIFIER);
        addToMap(ODTHighlightingTypes.PATH_DELIMITER, "ODT_Query//Path delimiter", DefaultLanguageHighlighterColors.KEYWORD);
        addToMap(ODTHighlightingTypes.IDENTIFIER, "ODT_Query//Step identifier", DefaultLanguageHighlighterColors.IDENTIFIER);
        addToMap(ODTTypes.CARET, "ODT_Query//Caret / Reverse", DefaultLanguageHighlighterColors.KEYWORD);

        highlightingMap.put(ODTTypes.VARIABLE_NAME, Variable);
        nonDisplayedHighlightingMap.put(ODTTypes.CURLY_OPEN, Braces);
        nonDisplayedHighlightingMap.put(ODTTypes.CURLY_CLOSED, Braces);
        nonDisplayedHighlightingMap.put(ODTTypes.BRACKET_OPEN, Brackets);
        nonDisplayedHighlightingMap.put(ODTTypes.BRACKET_CLOSED, Brackets);
        nonDisplayedHighlightingMap.put(ODTTypes.PARENTHESES_OPEN, Parentheses);
        nonDisplayedHighlightingMap.put(ODTTypes.PARENTHESES_CLOSE, Parentheses);
    }

    private static void addToMap(IElementType type, String name, TextAttributesKey fallback) {
        highlightingMap.put(type, createTextAttributesKey(name, fallback));
    }

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new ODTLexerAdapter(new ODTHighlightingLexer(null));
    }

    public static Collection<TextAttributesKey> getAttributes() {
        return new ArrayList<>(highlightingMap.values());
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return Optional.ofNullable(highlightingMap.get(tokenType))
                .or(() -> Optional.ofNullable(nonDisplayedHighlightingMap.get(tokenType)))
                .map(textAttributesKey -> new TextAttributesKey[] { textAttributesKey })
                .orElse(TextAttributesKey.EMPTY_ARRAY);
    }
}
