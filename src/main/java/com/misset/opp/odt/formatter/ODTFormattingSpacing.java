package com.misset.opp.odt.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ODTFormattingSpacing {

    private final SpacingBuilder spacingBuilder;

    public ODTFormattingSpacing(FormattingContext formattingContext) {
        final CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
        spacingBuilder = getDefaultSpacingBuilder(settings);
    }

    private SpacingBuilder getDefaultSpacingBuilder(@NotNull CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ODTLanguage.INSTANCE)
                // script
                .between(ODTTypes.SCRIPT_LINE, ODTTypes.SCRIPT_LINE).blankLines(0)
                .around(ODTTypes.SCRIPT_LINE).blankLines(0)
                // query
                .aroundInside(ODTTypes.STEP_SEPERATOR, ODTTypes.QUERY_PATH).spaces(1)
                ;
    }

    public Spacing computeSpacing(@NotNull Block parent, @Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(parent, child1, child2);
    }

    private boolean isNodeType(@Nullable Block block,
                               @Nullable IElementType elementType) {
        return Optional.ofNullable(block)
                .map(ODTFormattingBlock.class::cast)
                .map(AbstractBlock::getNode)
                .map(ASTNode::getElementType)
                .map(elementType::equals)
                .orElse(false);
    }
}
