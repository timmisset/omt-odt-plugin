package com.misset.opp.odt.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;

public class ODTNodeUtil {

    public static ASTNode prevVisible(ASTNode node) {
        node = node.getTreePrev();
        while (node != null && node.getElementType() == TokenType.WHITE_SPACE) {
            node = node.getTreePrev();
        }
        return node;
    }
}
