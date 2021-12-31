package com.misset.opp.omt;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.refactoring.OMTSupportsRefactoring;
import com.misset.opp.omt.psi.impl.refactoring.OMTSupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

public class OMTRefactoringSupport extends RefactoringSupportProvider {

    private OMTYamlDelegate getDelegate(PsiElement element) {
        if (element instanceof YAMLPsiElement) {
            return OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
        } else {
            return null;
        }
    }

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return getDelegate(context) instanceof OMTSupportsRefactoring;
    }

    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        OMTYamlDelegate delegate = getDelegate(element);
        return delegate instanceof OMTSupportsSafeDelete && ((OMTSupportsSafeDelete) delegate).isUnused();
    }
}
