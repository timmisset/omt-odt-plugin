package com.misset.opp.odt.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.ODTParserDefinition;
import com.misset.opp.odt.psi.ODTTypes;

public class ODTFormattingIndent {
    private static final TokenSet WITH_CHILD_INDENT = TokenSet.create(
            ODTTypes.COMMAND_BLOCK
    );
    private static final TokenSet INDENTED_WHEN_NOT_ROOT = TokenSet.create(
            ODTTypes.SCRIPT
    );
    private static final TokenSet QUERY_STEPS = TokenSet.create(
            ODTTypes.QUERY_OPERATION_STEP, ODTTypes.FORWARD_SLASH
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
            if (INDENTED_WHEN_NOT_ROOT.contains(elementType)) {
                return Indent.getNormalIndent();
            }
            if (isInside(node, ODTTypes.DEFINE_QUERY_STATEMENT) && QUERY_STEPS.contains(elementType)) {
                return Indent.getNormalIndent(false);
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
