package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YAMLOMTScalarList extends YAMLScalarListImpl implements InjectionHost {
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
    public InjectableContentType getInjectableContentType() {
        return YAMLInjectableUtil.getContentType(this);
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        return super.updateText(trimIndents(text));
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
