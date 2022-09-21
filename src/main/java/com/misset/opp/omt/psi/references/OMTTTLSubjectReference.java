package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTOntologyTypeProvider;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.util.TTLElementFinderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTTTLSubjectReference extends OMTPlainTextReference {
    public OMTTTLSubjectReference(@NotNull YAMLPlainTextImpl element,
                                  @NotNull
                                  TextRange textRange) {
        super(element, textRange);
    }

    private @Nullable String getFullyQualifiedUri() {
        YamlMetaType metaOwner = OMTMetaTypeProvider.getInstance(myElement.getProject()).getResolvedMetaType(myElement);
        return metaOwner != null ?
                ((OMTOntologyTypeProvider) metaOwner).getFullyQualifiedURI(myElement) :
                null;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        YAMLPlainTextImpl element = getElement();
        YamlMetaType metaOwner = OMTMetaTypeProvider.getInstance(element.getProject()).getResolvedMetaType(element);
        if (metaOwner instanceof OMTOntologyTypeProvider) {
            return TTLElementFinderUtil.getSubjectResolveResult(getElement().getProject(), getFullyQualifiedUri());
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof TTLLocalname) {
            String fullyQualifiedUri = getFullyQualifiedUri();
            return fullyQualifiedUri != null &&
                    fullyQualifiedUri.equals(((TTLLocalname) element).getQualifiedIri());
        }
        return super.isReferenceTo(element);
    }
}
