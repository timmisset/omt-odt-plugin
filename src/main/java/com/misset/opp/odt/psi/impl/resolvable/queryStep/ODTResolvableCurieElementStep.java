package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTResolvableCurieElementStep extends ODTResolvableQueryForwardStep implements ODTCurieElement {
    public ODTResolvableCurieElementStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getFullyQualifiedUri() {
        final ODTNamespacePrefixImpl namespacePrefix = (ODTNamespacePrefixImpl) getNamespacePrefix();
        final String fullyQualified;
        if (namespacePrefix != null) {
            fullyQualified = namespacePrefix.getFullyQualified(
                    Optional.ofNullable(PsiTreeUtil.nextVisibleLeaf(namespacePrefix))
                            .map(PsiElement::getText)
                            .orElse(""));
        } else {
            fullyQualified = getText().substring(1, getTextLength() - 1);
        }
        return fullyQualified;
    }
}
