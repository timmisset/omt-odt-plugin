package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.util.TTLElementFinderUtil;
import org.jetbrains.annotations.NotNull;

public class ODTTTLSubjectReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    public ODTTTLSubjectReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return TTLElementFinderUtil.getSubjectResolveResult(myElement.getProject(), myElement.getFullyQualifiedUri());
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof TTLLocalname) {
            String fullyQualifiedUri = myElement.getFullyQualifiedUri();
            return fullyQualifiedUri != null &&
                    fullyQualifiedUri.equals(((TTLLocalname) element).getQualifiedIri());
        }
        return super.isReferenceTo(element);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
