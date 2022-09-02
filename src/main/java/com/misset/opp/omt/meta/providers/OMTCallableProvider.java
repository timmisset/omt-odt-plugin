package com.misset.opp.omt.meta.providers;

import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.HashMap;

public interface OMTCallableProvider {

    /**
     * Returns a map with Callable items
     * if a value for host is provided, the callable cannot contain the host element. This is to prevent
     * an injected fragment receiving its own callable elements as response
     */
    @NotNull
    HashMap<String, Collection<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                            @Nullable PsiLanguageInjectionHost host);

    default HashMap<String, Collection<PsiCallable>> getCallableMap(YAMLMapping yamlMapping) {
        return getCallableMap(yamlMapping, null);
    }
}
