package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.ttl.OppModel;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTResolvableCurieElementStep extends ODTResolvableQueryForwardStep implements ODTCurieElement {
    public ODTResolvableCurieElementStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getFullyQualifiedUri() {
        final ODTNamespacePrefixImpl namespacePrefix = (ODTNamespacePrefixImpl) getNamespacePrefix();
        return namespacePrefix.getFullyQualifiedUri(
                Optional.ofNullable(PsiTreeUtil.nextVisibleLeaf(namespacePrefix))
                        .map(PsiElement::getText)
                        .orElse(null));
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        if(getFullyQualifiedUri() == null) {
            holder.registerProblem(getNamespacePrefix(), "Could not resolve prefix", ProblemHighlightType.ERROR);
        }
        super.inspect(holder);
    }
}
