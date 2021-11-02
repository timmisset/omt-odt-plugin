package com.misset.opp.odt.syntax;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.ODTLexerAdapter;
import com.misset.opp.odt.psi.ODTIgnored;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * SyntaxHighlighter works on the lexer output, this means that only the leaf AST nodes are queried.
 * Highlighting which cannot be resolved on that level must be added using the ODTHighlightingAnnotator
 * @see com.misset.opp.odt.annotation.ODTHighlightingAnnotator
 */
public class ODTSyntaxHighlighter implements SyntaxHighlighter {
    public static final TextAttributesKey BaseCallAttributesKey = createTextAttributesKey("Function call", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey DefineAttributesKey = createTextAttributesKey("Define statement name", DefaultLanguageHighlighterColors.INSTANCE_METHOD);

    private static final HashMap<IElementType, TextAttributesKey> highlightingMap = new HashMap<>();
    static {
        addToMap(ODTTypes.VARIABLE_NAME, "Variable", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
        addToMap(ODTTypes.STRING, "String", DefaultLanguageHighlighterColors.STRING);
        addToMap(ODTTypes.SEMICOLON, "Semicolon", DefaultLanguageHighlighterColors.SEMICOLON);
        addToMap(ODTIgnored.MULTILINE, "Comment (block)", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addToMap(ODTIgnored.JAVADOCS, "Comment (doc)", DefaultLanguageHighlighterColors.DOC_COMMENT);
        addToMap(ODTIgnored.END_OF_LINE_COMMENT, "Comment (line)", DefaultLanguageHighlighterColors.LINE_COMMENT);
    }

    private static void addToMap(IElementType type, String name, TextAttributesKey fallback) {
        highlightingMap.put(type, createTextAttributesKey(name, fallback));
    }

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new ODTLexerAdapter();
    }
    public static Collection<TextAttributesKey> getAttributes() {
        final ArrayList<TextAttributesKey> textAttributesKeys = new ArrayList<>(highlightingMap.values());
        textAttributesKeys.add(BaseCallAttributesKey);
        textAttributesKeys.add(DefineAttributesKey);
        return textAttributesKeys;
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return Optional.ofNullable(highlightingMap.get(tokenType))
                .map(textAttributesKey -> new TextAttributesKey[] { textAttributesKey })
                .orElse(TextAttributesKey.EMPTY_ARRAY);
    }
}
