package com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.inspection.quikfix.ODTRegisterPrefixLocalQuickFix;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import com.misset.opp.omt.inspection.quickfix.OMTRegisterPrefixLocalQuickFix;
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
    protected PsiElement getAnnotationRange() {
        return getLastChild();
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        boolean injectedInOMT = ODTInjectionUtil.getInjectionHost(holder.getFile()) != null;
        if(getFullyQualifiedUri() == null) {
            final PsiElement prefix = getNamespacePrefix().getFirstChild();
            holder.registerProblem(prefix,
                    "Could not resolve prefix",
                    ProblemHighlightType.ERROR,
                    OMTPrefixIndex.getNamespaces(prefix.getText())
                            .stream()
                            .map(namespace -> getLocalQuikFix(injectedInOMT, prefix.getText(), namespace))
                            .toArray(LocalQuickFix[]::new)
            );
        }
        super.inspect(holder);
    }
    private LocalQuickFix getLocalQuikFix(boolean injectedInOMT, String prefix, String namespace) {
        if(injectedInOMT) {
            return new OMTRegisterPrefixLocalQuickFix(prefix, namespace);
        } else {
            return new ODTRegisterPrefixLocalQuickFix(prefix, namespace);
        }
    }
}
