package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTParamTypeReference extends OMTPlainTextReference {
    public OMTParamTypeReference(@NotNull YAMLPlainTextImpl element,
                                 @NotNull
                                         TextRange textRange) {
        super(element, textRange);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final String prefix = textRange.substring(myElement.getText());
        return OMTMetaTreeUtil
                .resolveProvider(myElement, OMTPrefixProvider.class, prefix, OMTPrefixProvider::getPrefixMap)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
