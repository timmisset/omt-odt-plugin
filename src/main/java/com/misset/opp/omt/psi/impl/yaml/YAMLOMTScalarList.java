package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
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
}
