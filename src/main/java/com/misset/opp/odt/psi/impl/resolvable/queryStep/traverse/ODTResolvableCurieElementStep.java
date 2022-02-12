package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTResolvableCurieElementStep extends ODTResolvableQueryForwardStep implements ODTCurieElement {
    public ODTResolvableCurieElementStep(@NotNull ASTNode node) {
        super(node);
    }

    private static final Logger LOGGER = Logger.getInstance(ODTResolvableCurieElementStep.class);

    @Override
    public String calculateFullyQualifiedUri() {
        return LoggerUtil.computeWithLogger(LOGGER, "Calculating URI from Curie for " + getText(), () -> {
            final ODTNamespacePrefixImpl namespacePrefix = (ODTNamespacePrefixImpl) getNamespacePrefix();
            return namespacePrefix.getFullyQualifiedUri(
                    Optional.ofNullable(PsiTreeUtil.nextVisibleLeaf(namespacePrefix))
                            .map(PsiElement::getText)
                            .orElse(null));
        });
    }

    @Override
    protected PsiElement getAnnotationRange() {
        return getLastChild();
    }

    @Override
    protected TextRange getModelReferenceTextRange() {
        return getLastChild().getTextRangeInParent();
    }
}
