package com.misset.opp.odt.syntax;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
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
 *
 * @see com.misset.opp.odt.annotation.ODTHighlightingAnnotator
 */
public class ODTSyntaxHighlighter implements SyntaxHighlighter {
    public static final TextAttributesKey BaseCallAttributesKey = createTextAttributesKey("ODT_Function call",
            DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey DefineAttributesKey = createTextAttributesKey("ODT_Define statement name",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey OntologyClassAttributesKey = createTextAttributesKey(
            "ODT_Ontology Class (pol:Dossier)",
            DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey OntologyInstanceAttributesKey = createTextAttributesKey(
            "ODT_Ontology Instance (instance of pol:Dossier)",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey OntologyTypeAttributesKey = createTextAttributesKey(
            "ODT_Ontology Type (xsd:string)",
            DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey OntologyValueAttributesKey = createTextAttributesKey(
            "ODT_Ontology Value ('a string')",
            DefaultLanguageHighlighterColors.CONSTANT);

    private static final HashMap<IElementType, TextAttributesKey> highlightingMap = new HashMap<>();

    static {
        addToMap(ODTTypes.VARIABLE_NAME, "ODT_Variable", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
        addToMap(ODTTypes.STRING, "ODT_String", DefaultLanguageHighlighterColors.STRING);
        addToMap(ODTTypes.SEMICOLON, "ODT_Semicolon", DefaultLanguageHighlighterColors.SEMICOLON);
        addToMap(ODTIgnored.MULTILINE, "ODT_Comment (block)", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        addToMap(JavaDocElementType.DOC_COMMENT, "ODT_Comment (doc)", DefaultLanguageHighlighterColors.DOC_COMMENT);
        addToMap(ODTIgnored.END_OF_LINE_COMMENT, "ODT_Comment (line)", DefaultLanguageHighlighterColors.LINE_COMMENT);
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
        textAttributesKeys.add(OntologyClassAttributesKey);
        textAttributesKeys.add(OntologyTypeAttributesKey);
        textAttributesKeys.add(OntologyInstanceAttributesKey);
        textAttributesKeys.add(OntologyValueAttributesKey);
        return textAttributesKeys;
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return Optional.ofNullable(highlightingMap.get(tokenType))
                .map(textAttributesKey -> new TextAttributesKey[] { textAttributesKey })
                .orElse(TextAttributesKey.EMPTY_ARRAY);
    }
}
