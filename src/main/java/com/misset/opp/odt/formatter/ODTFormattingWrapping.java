package com.misset.opp.odt.formatter;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTTypes;

public class ODTFormattingWrapping {
    public static boolean isIncomplete(ASTNode node) {
        return isIncompleteCommandBlock(node);
    }

    private static boolean isIncompleteCommandBlock(ASTNode node) {
        return
                node.getElementType() == ODTTypes.COMMAND_BLOCK &&
                        node.getText().replace("\n", "").equals("{}");
    }

}
