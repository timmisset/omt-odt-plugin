package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.reference.ODTNamespacePrefixReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public abstract class ODTBaseNamespacePrefix extends ASTWrapperPsiElement implements ODTNamespacePrefix {
    public ODTBaseNamespacePrefix(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return replace(ODTElementGenerator.getInstance(getProject()).createNamespacePrefix(name));
    }

    @Override
    @NotNull
    public String getName() {
        return getFirstChild().getText();
    }

    @Override
    public PsiReference getReference() {
        if(getParent() instanceof ODTDefinePrefix) { return null; }
        return new ODTNamespacePrefixReference(this);
    }

    /**
     * Returns a fully qualified URI by resolving the prefix to a namespace and appending the localname
     */
    public String getFullyQualifiedUri(String localName) {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(element -> element instanceof YAMLKeyValue ?
                        PrefixUtil.getFullyQualifiedUri((YAMLKeyValue) element, localName) :
                        PrefixUtil.getFullyQualifiedUri((ODTDefinePrefix) element.getParent(), localName))
                .orElse(null);
    }

}
