package com.misset.opp.omt.psi.references;

import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.LinkedHashMap;
import java.util.Objects;

public class OMTCallableReference extends OMTPlainTextReference {

    public OMTCallableReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        LinkedHashMap<YAMLMapping, OMTCallableProvider> providerMap =
                OMTMetaTreeUtil.collectMetaParents(myElement, YAMLMapping.class, OMTCallableProvider.class, false, Objects::isNull);
        return OMTMetaTreeUtil.resolveProvider(providerMap, myElement.getText(), OMTCallableProvider::getCallableMap)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
