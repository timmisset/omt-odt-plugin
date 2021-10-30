package com.misset.opp.omt.psi.references;

import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import static com.misset.opp.omt.meta.OMTMetaTreeUtil.resolveProvider;

public class OMTOntologyPrefixReference extends OMTPlainTextReference {
    public OMTOntologyPrefixReference(YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return resolveProvider(myElement, OMTPrefixProvider.class, myElement.getText(), OMTPrefixProvider::getPrefixMap)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
