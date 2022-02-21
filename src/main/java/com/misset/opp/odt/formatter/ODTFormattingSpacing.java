package com.misset.opp.odt.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

import static com.misset.opp.odt.formatter.ODTTokenSets.QUERY_TYPES;

public class ODTFormattingSpacing {

    private final SpacingBuilder spacingBuilder;

    private static final TokenSet SIGNATURE_ARGUMENTS = TokenSet.orSet(QUERY_TYPES, TokenSet.create(
            ODTTypes.SIGNATURE_ARGUMENT, ODTTypes.COMMAND_CALL));

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
                .between(ODTTypes.IF_OPERATOR, QUERY_TYPES).spaces(1)
                .between(ODTTypes.ELSE_OPERATOR, ODTTypes.COMMAND_BLOCK).spacing(1, 1, 0, false, 0)
                .between(ODTTypes.COMMAND_BLOCK, ODTTypes.ELSE_BLOCK).spacing(1, 1, 0, false, 0)
                .between(ODTTypes.COMMAND_BLOCK, ODTTypes.ELSE_OPERATOR).spacing(1, 1, 0, false, 0)

                // query
                .around(ODTTypes.LAMBDA).spaces(1)
                .aroundInside(ODTTypes.STEP_SEPERATOR, ODTTypes.QUERY_PATH).spaces(1)
                .aroundInside(ODTTypes.ROOT_INDICATOR, ODTTypes.QUERY_PATH).spaces(0)
                .before(ODTTokenSets.CHOOSE_BLOCKS).spacing(0, 0, 1, false, 1)

                // define param
                .between(ODTTypes.COMMA, ODTTypes.VARIABLE).spaces(1)
                .between(ODTTypes.VARIABLE, ODTTypes.COMMA).spaces(0)
                .between(ODTTypes.PARENTHESES_OPEN, ODTTypes.VARIABLE).spaces(0)
                .between(ODTTypes.VARIABLE, ODTTypes.PARENTHESES_CLOSE).spaces(0)

                // calls
                .around(ODTTypes.FLAG_SIGNATURE).spaces(0)
                .after(ODTTypes.AT).spaces(0)

                // signature
                .between(ODTTypes.COMMA, SIGNATURE_ARGUMENTS).spaces(1)
                .between(SIGNATURE_ARGUMENTS, ODTTypes.COMMA).spaces(0)
                .between(ODTTypes.PARENTHESES_OPEN, SIGNATURE_ARGUMENTS).spaces(0)
                .between(SIGNATURE_ARGUMENTS, ODTTypes.PARENTHESES_CLOSE).spaces(0)

                // relational operators
                .around(ODTTokenSets.RELATIONAL_OPERATORS).spaces(1)


                // make sure inserted javadoc elements are automatically formatted correctly
                .before(JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS).spacing(0, 0, 1, false, 1)
                .before(JavaDocTokenType.DOC_COMMENT_END).spacing(0, 0, 1, false, 1)


                ;
    }

    public Spacing computeSpacing(@NotNull Block parent, @Nullable Block child1, @NotNull Block child2) {
        parent = unwrap(parent);
        child1 = unwrap(child1);
        child2 = unwrap(child2);
        return spacingBuilder.getSpacing(parent, child1, child2);
    }

    public Block unwrap(Block block) {
        if (block instanceof ODTFormattingBlock) {
            return block;
        }
        try {
            Method getOriginal = block.getClass().getDeclaredMethod("getOriginal");
            getOriginal.setAccessible(true);
            return (Block) getOriginal.invoke(block);
        } catch (Exception e) {
            return block;
        }
    }
}
