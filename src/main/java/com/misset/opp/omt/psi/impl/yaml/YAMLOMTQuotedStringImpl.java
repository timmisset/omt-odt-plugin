package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.injection.InjectableContentType;
import com.misset.opp.omt.injection.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLQuotedText;
import org.jetbrains.yaml.psi.impl.YAMLQuotedTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;
import org.jetbrains.yaml.psi.impl.YamlScalarTextEvaluator;

import java.util.List;

/**
 * Since the YAMLQuotedText is a final class we cannot extend it and use a delegate instead
 */
public class YAMLOMTQuotedStringImpl extends YAMLScalarImpl implements YAMLQuotedText, InjectionHost {

    private final YAMLQuotedTextImpl delegate;

    public YAMLOMTQuotedStringImpl(@NotNull ASTNode node) {
        super(node);
        delegate = new YAMLQuotedTextImpl(node);
    }

    @Override
    public List<TextRange> getTextRanges() {
        return YAMLInjectableUtil.getTextRanges(this);
    }

    @Override
    public String getPrefix() {
        return getText().substring(0, 1);
    }

    @Override
    public String getSuffix() {
        return getText().substring(this.getTextLength() - 1);
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

    @Override
    public InjectableContentType getInjectableContentType() {
        return YAMLInjectableUtil.getContentType(this);
    }
}
