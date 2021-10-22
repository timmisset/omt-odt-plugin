package com.misset.opp.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

public class ASTNodeUtil {

    /**
     * Returns the first parent that matches any of the given types
     */
    @Nullable
    public static IElementType getParentType(ASTNode node,
                                             TokenSet tokenSet) {
        while(node != null && !tokenSet.contains(node.getElementType())) {
            node = node.getTreeParent();
        }
        return node != null ? node.getElementType() : null;
    }

}
