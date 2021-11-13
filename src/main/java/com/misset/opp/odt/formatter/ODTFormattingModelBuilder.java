package com.misset.opp.odt.formatter;

import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class ODTFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull
    FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider
                .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                        new ODTFormattingBlock(formattingContext.getNode(),
                                new ODTFormattingContext(formattingContext)),
                        settings);
    }
}
