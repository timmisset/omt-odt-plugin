package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.List;

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
        String original = getNode().getText();
        int commonPrefixLength = StringUtil.commonPrefixLength(original, text);
        int commonSuffixLength = StringUtil.commonSuffixLength(original, text);
        int indent = locateIndent();

        ASTNode scalarEol = getNode().findChildByType(YAMLTokenTypes.SCALAR_EOL);
        if (scalarEol == null) {
            // a very strange situation
            return super.updateText(text);
        }

        int eolOffsetInParent = scalarEol.getStartOffsetInParent();

        int startContent = eolOffsetInParent + indent + 1;

        String originalRowPrefix = original.substring(startContent, commonPrefixLength);
        String indentString = StringUtil.repeatSymbol(' ', indent);

        String prefix = originalRowPrefix.replaceAll("\n" + indentString, "\n");
        String suffix = text.substring(text.length() - commonSuffixLength).replaceAll("\n" + indentString, "\n");

        String result = prefix + text.substring(commonPrefixLength, text.length() - commonSuffixLength) + suffix;
        return super.updateText(result);
    }
}
