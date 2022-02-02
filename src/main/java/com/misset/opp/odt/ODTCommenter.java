package com.misset.opp.odt;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ODTCommenter implements Commenter {
    private static final List<String> PREFIXES = List.of("#", "//");

    @Override
    public @Nullable String getLineCommentPrefix() {
        return "#";
    }

    @Override
    public @NotNull List<String> getLineCommentPrefixes() {
        return PREFIXES;
    }

    @Override
    public @Nullable String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public @Nullable String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public @Nullable String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentSuffix() {
        return null;
    }
}
