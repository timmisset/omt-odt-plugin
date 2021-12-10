package com.misset.opp.odt.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.JavaDocTokenType;
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
                .between(ODTTypes.CURLY_OPEN, ODTTypes.SCRIPT).spacing(0, 0, 1, false, 0)
                .between(ODTTypes.SCRIPT, ODTTypes.CURLY_CLOSED).spacing(0, 0, 1, false, 0)
                .between(ODTTypes.SCRIPT_LINE, ODTTypes.SCRIPT_LINE).spacing(0, 0, 0, true, 1)
                // condition block
                .between(ODTTypes.IF_BLOCK, ODTTypes.COMMAND_BLOCK).spacing(1, 1, 0, false, 0)
                .between(ODTTypes.IF_OPERATOR, ODTTokenSets.QUERY_TYPES).spaces(1)
                .between(ODTTypes.ELSE_OPERATOR, ODTTypes.COMMAND_BLOCK).spacing(1, 1, 0, false, 0)
                .between(ODTTypes.COMMAND_BLOCK, ODTTypes.ELSE_BLOCK).spacing(1, 1, 0, false, 0)
                .between(ODTTypes.COMMAND_BLOCK, ODTTypes.ELSE_OPERATOR).spacing(1, 1, 0, false, 0)

                // query
                .around(ODTTypes.LAMBDA).spaces(1)
                .aroundInside(ODTTypes.STEP_SEPERATOR, ODTTypes.QUERY_PATH).spaces(1)
                .aroundInside(ODTTypes.ROOT_INDICATOR, ODTTypes.QUERY_PATH).spaces(0)
                .before(ODTTokenSets.CHOOSE_BLOCKS).spacing(0, 0, 1, false, 1)

                // signature
                .between(ODTTypes.COMMA, ODTTypes.SIGNATURE_ARGUMENT).spaces(1)
                .between(ODTTypes.SIGNATURE_ARGUMENT, ODTTypes.COMMA).spaces(0)
                .between(ODTTypes.PARENTHESES_OPEN, ODTTypes.SIGNATURE_ARGUMENT).spaces(0)
                .between(ODTTypes.SIGNATURE_ARGUMENT, ODTTypes.PARENTHESES_CLOSE).spaces(0)

                // assignment operators
                .around(ODTTokenSets.ASSIGNMENT_OPERATORS).spaces(1)

                // make sure inserted javadoc elements are automatically formatted correctly
                .before(JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS).spacing(0, 0, 1, false, 1)
                .before(JavaDocTokenType.DOC_COMMENT_END).spacing(0, 0, 1, false, 1)


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
