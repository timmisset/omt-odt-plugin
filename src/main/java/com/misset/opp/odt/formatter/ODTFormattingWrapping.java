package com.misset.opp.odt.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTTypes;

public class ODTFormattingWrapping {

    private ODTFormattingWrapping() {

    }

    /**
     * Returns true for incomplete containers without closing tag:
     * - { Command Block without }
     * - ( Parameter List without )
     * - [ Filter without ]
     * And ScriptLine without semicolon:
     */
    public static boolean isIncomplete(ASTNode node) {
        IElementType elementType = node.getElementType();
        IElementType lastVisible = getLastVisible(node);
        if (lastVisible == null) {
            return false;
        }
        if (elementType == ODTTypes.SCRIPT_LINE && lastVisible != ODTTypes.SEMICOLON) {
            return true;
        }
        return FormatterUtil.isIncomplete(node);
    }

    private static IElementType getLastVisible(ASTNode node) {
        ASTNode lastChild = node == null ? null : node.getLastChildNode();
        while (lastChild != null && lastChild.getElementType() == TokenType.WHITE_SPACE) {
            lastChild = lastChild.getTreePrev();
        }
        return lastChild != null ? lastChild.getElementType() : null;
    }

}
