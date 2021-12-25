package com.misset.opp.odt.formatter;

import com.intellij.application.options.CodeStyle;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.misset.opp.odt.ODTLanguage;
import org.jetbrains.annotations.NotNull;

public class ODTFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull
    FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        setIndentAndContinuationIndent(formattingContext.getProject());

        return FormattingModelProvider
                .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                        new ODTFormattingBlock(formattingContext.getNode(),
                                new ODTFormattingContext(formattingContext)),
                        null);
    }

    /**
     * Set the ODT indent sizes as common language settings
     * todo: make this a configuration setting to be less opinionated
     */
    private void setIndentAndContinuationIndent(Project project) {
        CodeStyleSettings settings = CodeStyle.getSettings(project);
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(ODTLanguage.INSTANCE);
        CommonCodeStyleSettings.IndentOptions indentOptions = commonSettings.getIndentOptions();
        if (indentOptions == null) {
            return;
        }
        indentOptions.INDENT_SIZE = 4;
        indentOptions.CONTINUATION_INDENT_SIZE = 4;
    }
}
