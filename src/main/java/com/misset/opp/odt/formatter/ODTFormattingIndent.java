package com.misset.opp.odt.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.ODTParserDefinition;
import com.misset.opp.odt.psi.ODTTypes;

public class ODTFormattingIndent {
    private static final TokenSet WITH_CHILD_INDENT = TokenSet.create(
            ODTTypes.COMMAND_BLOCK
    );
    private static final TokenSet QUERY_STEPS = TokenSet.create(
            ODTTypes.QUERY_OPERATION_STEP, ODTTypes.FORWARD_SLASH, ODTTypes.BOOLEAN_OPERATOR
    );
    private static final TokenSet JAVA_DOC_INDENTED = TokenSet.create(
            JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS,
            JavaDocTokenType.DOC_COMMENT_END
    );

    private static boolean isRootElement(ASTNode node) {
        ASTNode treeParent = node.getTreeParent();
        return treeParent != null && treeParent.getElementType() == ODTParserDefinition.ODTFileElementType;
    }

    private static boolean isInside(ASTNode node, IElementType insideType) {
        while (node != null && node.getElementType() != insideType) {
            node = node.getTreeParent();
        }
        return node != null;
    }

    public static Indent computeIndent(ASTNode node) {
        if (!isRootElement(node)) {
            IElementType elementType = node.getElementType();
            if (elementType == ODTTypes.SCRIPT) {
                return Indent.getNormalIndent();
            } else if (ODTTokenSets.CHOOSE_INDENTED_OPERATORS.contains(elementType)) {
                return Indent.getNormalIndent(true);
            } else if (isInside(node, ODTTypes.DEFINE_QUERY_STATEMENT) && QUERY_STEPS.contains(elementType)) {
                return Indent.getNormalIndent(false);
            } else if (JAVA_DOC_INDENTED.contains(elementType)) {
                return Indent.getSpaceIndent(1);
            }
        }
        return Indent.getNoneIndent();
    }

    public static Indent computeChildIndent(ASTNode node) {
        IElementType elementType = node.getElementType();
        if (WITH_CHILD_INDENT.contains(elementType)) {
            return Indent.getNormalIndent();
        } else {
            return Indent.getNoneIndent();
        }
    }
}
