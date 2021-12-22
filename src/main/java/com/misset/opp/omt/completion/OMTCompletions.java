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

/**
 * Generic completion contributor for the entire OMT Structure.
 * The actual completion calculations are performed by the MetaTypes.
 * <p>
 * The base class YamlMetaTypeCompletionProviderBase has as a shortcoming that it can call key completions
 * on an empty map. In this case the MetaType, which has no direct PsiElement context, is called with a null value
 * for the map. This makes it impossible to get any context required to set completion LookupElements.
 * To fix this, this wrapper class registers the CompletionParameters that are created initially by the framework.
 * Luckily, the entire completion process happens inside a single (main) thread making this a thread-safe way of
 * having that information statically available.
 */
public class OMTCompletions extends CompletionContributor {
    /*
        Sometimes we need access to the original element / file
        The Yaml completions do not provide this information to the meta-types so they can be retrieved via this class
     */
    private static final HashMap<PsiElement, CompletionParameters> completionParametersHashMap = new HashMap<>();

    public OMTCompletions() {
        extend(CompletionType.BASIC, psiElement(), new OMTKeyCompletion());
    }

    public static CompletionParameters getCompletionParameters(PsiElement element) {
        return completionParametersHashMap.get(element);
    }

    public static void registerCompletionParameters(CompletionParameters parameters) {
        completionParametersHashMap.put(parameters.getPosition(), parameters);
    }

    public static PsiElement getPlaceholderToken() {
        return completionParametersHashMap.keySet().stream().findFirst().orElse(null);
    }

    public static void removeCompletionParameters(CompletionParameters parameters) {
        completionParametersHashMap.remove(parameters.getPosition());
    }

    static class OMTKeyCompletion extends YamlMetaTypeCompletionProviderBase {
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
