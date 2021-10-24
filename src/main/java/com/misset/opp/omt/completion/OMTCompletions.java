package com.misset.opp.omt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeCompletionProviderBase;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;

import java.util.HashMap;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class OMTCompletions extends CompletionContributor {
    /*
        Sometimes we need access to the original element / file
        The Yaml completions do not provide this information to the meta-types so they can be retrieved via this class
     */
    private static HashMap<PsiElement, CompletionParameters> completionParametersHashMap = new HashMap<>();

    public OMTCompletions() {
        extend(CompletionType.BASIC, psiElement(), new OMTKeyCompletion());
    }

    public static CompletionParameters getCompletionParameters(PsiElement element) {
        return completionParametersHashMap.get(element);
    }

    public static void registerCompletionParameters(CompletionParameters parameters) {
        completionParametersHashMap.put(parameters.getPosition(), parameters);
    }

    public static void removeCompletionParameters(CompletionParameters parameters) {
        completionParametersHashMap.remove(parameters.getPosition());
    }

    class OMTKeyCompletion extends YamlMetaTypeCompletionProviderBase {
        @Override
        protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull CompletionParameters params) {
            return OMTMetaTypeProvider.getInstance(params.getPosition().getProject());
        }

        @Override
        protected void addCompletions(@NotNull CompletionParameters params,
                                      @NotNull ProcessingContext context,
                                      @NotNull CompletionResultSet result) {
            OMTCompletions.registerCompletionParameters(params);
            super.addCompletions(params, context, result);
            OMTCompletions.removeCompletionParameters(params);
        }

    }
}
