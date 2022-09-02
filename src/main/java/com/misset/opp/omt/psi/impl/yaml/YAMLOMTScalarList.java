package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.omt.injection.InjectionHost;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YAMLOMTScalarList extends YAMLScalarListImpl implements InjectionHost {
    private static final Logger LOGGER = Logger.getInstance(YAMLOMTScalarList.class);
    public YAMLOMTScalarList(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public List<TextRange> getTextRanges() {
        return YAMLInjectableUtil.getTextRanges(this);
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        return LoggerUtil.computeWithLogger(LOGGER, "handleContentChange", () -> {
            String trimmedContent = trimIndents(text);
            return ElementManipulators.handleContentChange(this, trimmedContent);
        });
    }

    private String trimIndents(String text) {
        String[] rows = text.split("\n");
        List<String> strings = Arrays.stream(rows)
                .map(String::trim)
                .collect(Collectors.toList());
        if (strings.get(0).equals("|")) {
            strings.remove(0);
        }
        String trimmedText = String.join("\n", strings);
        if (text.endsWith("\n")) {
            trimmedText += "\n";
        }
        return trimmedText;
    }
}
