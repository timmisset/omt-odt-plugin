package com.misset.opp.omt.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeCompletionProviderBase;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;

public class OMTKeyCompletion extends YamlMetaTypeCompletionProviderBase {

    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull CompletionParameters params) {
        return new OMTMetaTypeProvider();
    }
}
