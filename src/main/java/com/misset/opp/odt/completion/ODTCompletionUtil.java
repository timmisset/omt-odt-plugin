package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTCompletionUtil {

    private ODTCompletionUtil() {
        // empty constructor
    }

    /**
     * Returns a prefix matcher for the scenario where a curie is used
     * <p>
     * Based on the container class a plain text substring is extract to be used instead
     * ont:Cl<caret>assA --> prefixMatcher = "ont:Cl"
     * ^ont:cl<caret>assA --> prefixMatcher = "^ont:cl"
     * It's important that the containerClass contains any additional prefix symbols that should
     * be required for the final prefixMatcher.
     * For example, if a root indicator "/" is also required, use a container that includes this symbol
     * The same for the caret/reverse indicator "^"
     */
    public static <T extends PsiElement> String getCuriePrefixMatcher(@NotNull CompletionParameters parameters,
                                                                      @NotNull CompletionResultSet result,
                                                                      Class<T> containerClass) {
        String substring = getPrefixMatcherSubstring(parameters, containerClass);
        if (substring == null) {
            return result.getPrefixMatcher().getPrefix();
        }
        int correctedOffset = substring.endsWith(":") && result.getPrefixMatcher().getPrefix().startsWith(":") ? 1 : 0;
        return substring.substring(0, substring.length() - correctedOffset) + result.getPrefixMatcher().getPrefix();
    }

    /**
     * Returns a prefix matcher substring based on caret position and parent class
     * This includes any special symbols, it doesn't contain the existing prefixMatcher.
     * Returns null when the container class cannot be found
     */
    @Nullable
    public static <T extends PsiElement> String getPrefixMatcherSubstring(@NotNull CompletionParameters parameters,
                                                                          Class<T> containerClass) {
        PsiElement position = parameters.getPosition();
        T container = PsiTreeUtil.getParentOfType(position, containerClass);
        if (container == null) {
            return null;
        }
        int startOffset = container.getTextOffset();
        int currentOffset = position.getTextOffset();
        return parameters.getEditor().getDocument().getText().substring(startOffset, currentOffset);
    }
}
