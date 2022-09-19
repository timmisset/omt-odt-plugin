package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTResolvableCurieElementStepAbstract extends ODTResolvableQueryForwardStepAbstract implements ODTCurieElement {
    private static final Logger LOGGER = Logger.getInstance(ODTCurieElement.class);

    protected ODTResolvableCurieElementStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

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
    public PsiElement getAnnotationRange() {
        return getLastChild();
    }

    @Override
    public TextRange getModelReferenceTextRange() {
        return getLastChild().getTextRangeInParent();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        String prefix = getNamespacePrefix().getName();
        ODTCurieElement odtCurieElement = ODTElementGenerator.getInstance(getProject()).fromFile(String.format("%s:%s", prefix, name), ODTCurieElement.class);
        return replace(odtCurieElement);
    }
}
