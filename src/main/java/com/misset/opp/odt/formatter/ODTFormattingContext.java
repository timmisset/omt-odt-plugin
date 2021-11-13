package com.misset.opp.odt.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The formatter has 2 main functions, indentation and alignment
 * The indentation is used to indent a block (relative to its parent).
 * The element that is indented should generate an alignment token that can be used by subsequent items as an anchor
 * Even when these items have indentations, the alignment will overrule it and make sure identical
 * alignment tokens are levelled.
 * <p>
 * There is some complexity in this code due to the levelling of query statements which are not
 * simply processed as YAML scalar values but have certain additional indentation and alignment
 * based on their query(step) types.
 */
public class ODTFormattingContext {

    private final ODTFormattingSpacing formattingSpacing;
    private final ODTFormattingAlignment formattingAlignment;

    public ODTFormattingContext(FormattingContext formattingContext) {
        formattingSpacing = new ODTFormattingSpacing(formattingContext);
        formattingAlignment = new ODTFormattingAlignment();
    }

    public Spacing computeSpacing(@NotNull Block parent, @Nullable Block child1, @NotNull Block child2) {
        return formattingSpacing.computeSpacing(parent, child1, child2);
    }

    public Alignment computeAlignment(@NotNull ASTNode node) {
        return formattingAlignment.computeAlignment(node);
    }

}

