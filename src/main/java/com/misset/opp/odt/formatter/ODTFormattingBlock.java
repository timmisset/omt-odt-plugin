package com.misset.opp.odt.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ODTFormattingBlock extends AbstractBlock {
    private final ODTFormattingContext formattingContext;

    protected ODTFormattingBlock(@NotNull ASTNode node,
                                 @NotNull ODTFormattingContext formattingContext) {
        super(node, null, formattingContext.computeAlignment(node));
        this.formattingContext = formattingContext;
    }

    @Override
    protected List<Block> buildChildren() {
        return buildChildren(myNode);
    }

    private @NotNull List<Block> buildChildren(@NotNull ASTNode node) {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = node.getFirstChildNode();
        while (child != null) {
            if (!TokenType.WHITE_SPACE.equals(child.getElementType())) {
                blocks.add(new ODTFormattingBlock(child, formattingContext));
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1,
                                        @NotNull Block child2) {
        return formattingContext.computeSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Indent getIndent() {
        return super.getIndent();
    }
}
