package com.misset.opp.odt.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTFormattingSpacing {

    private final SpacingBuilder spacingBuilder;

    public ODTFormattingSpacing(FormattingContext formattingContext) {
        final CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
        spacingBuilder = getDefaultSpacingBuilder(settings);
    }

    private SpacingBuilder getDefaultSpacingBuilder(@NotNull CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ODTLanguage.INSTANCE)
                .around(ODTTypes.SCRIPT_LINE).blankLines(0)
                .around(ODTTypes.SCRIPT_LINE_WITH_SEMICOLON).blankLines(0);
    }

    public Spacing computeSpacing(@NotNull Block parent, @Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(parent, child1, child2);
    }
}
