package com.misset.opp.odt.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.ODTParserDefinition;
import com.misset.opp.odt.psi.ODTTypes;

import java.util.Optional;

public class ODTFormattingIndent {

    private ODTFormattingIndent() {
        // empty constructor
    }

    private static final TokenSet JAVA_DOC_INDENTED = TokenSet.create(
            JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS,
            JavaDocTokenType.DOC_COMMENT_END
    );
    private static final TokenSet CLOSING_ENCAPSULATIONS = TokenSet.create(
            ODTTypes.CURLY_CLOSED, ODTTypes.BRACKET_CLOSED, ODTTypes.PARENTHESES_CLOSE,
            ODTTypes.END_PATH
    );

    public static Indent computeIndent(ASTNode node) {
        IElementType parentType = getParentType(node);
        IElementType elementType = node.getElementType();
        if (elementType == ODTTypes.SCRIPT_LINE ||
                // Scriptline with DocComment is a multiline scriptline and will get indented otherwise
                parentType == ODTTypes.SCRIPT_LINE ||
                // Script with comments is a multiline scriptline
                parentType == ODTTypes.SCRIPT ||
                parentType == ODTParserDefinition.ODTFileElementType ||
                CLOSING_ENCAPSULATIONS.contains(elementType)) {
            return Indent.getNoneIndent();
        } else if (JAVA_DOC_INDENTED.contains(elementType)) {
            return Indent.getSpaceIndent(1);
        }
        return null;
    }

    @SuppressWarnings("java:S2637")
    private static IElementType getParentType(ASTNode node) {
        return Optional.ofNullable(node.getTreeParent())
                .map(ASTNode::getElementType)
                // suppressing warning 2637
                .orElse(null);
    }

    public static Indent computeChildIndent(ASTNode node) {
        if (ODTFormattingWrapping.isIncomplete(node)) {
            return null;
        }
        IElementType elementType = node.getElementType();
        if (elementType == ODTParserDefinition.ODTFileElementType ||
                elementType == ODTTypes.SCRIPT ||
                elementType == ODTTypes.DEFINE_COMMAND_STATEMENT) {
            return Indent.getNoneIndent();
        }
        return null;
    }
}
