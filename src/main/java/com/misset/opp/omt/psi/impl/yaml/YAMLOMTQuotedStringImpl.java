package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLQuotedText;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLQuotedTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;
import org.jetbrains.yaml.psi.impl.YamlScalarTextEvaluator;

import java.util.List;

/**
 * Since the YAMLQuotedText is a final class we cannot extend it and use a delegate instead
 */
public class YAMLOMTQuotedStringImpl extends YAMLScalarImpl implements YAMLQuotedText, PsiLanguageInjectionHost, YAMLScalar {

    private final YAMLQuotedTextImpl delegate;

    public YAMLOMTQuotedStringImpl(@NotNull ASTNode node) {
        super(node);
        delegate = new YAMLQuotedTextImpl(node);
    }

    @Override
    public boolean isSingleQuote() {
        return delegate.isSingleQuote();
    }

    @Override
    public @NotNull List<TextRange> getContentRanges() {
        return delegate.getContentRanges();
    }

    @Override
    public @NotNull YamlScalarTextEvaluator getTextEvaluator() {
        return delegate.getTextEvaluator();
    }

    @Override
    public boolean isMultiline() {
        return delegate.isMultiline();
    }

}
