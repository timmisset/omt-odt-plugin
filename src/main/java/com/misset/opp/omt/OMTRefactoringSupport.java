package com.misset.opp.omt;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlVariableDelegate;
import com.misset.opp.shared.refactoring.SupportsRefactoring;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        return getDelegate(context) instanceof SupportsRefactoring;
    }

    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        OMTYamlDelegate delegate = getDelegate(element);
        return delegate instanceof SupportsSafeDelete && ((SupportsSafeDelete) delegate).isUnused();
    }

    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
        OMTYamlDelegate delegate = getDelegate(element);
        return delegate instanceof OMTYamlVariableDelegate;
    }

    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, @Nullable PsiElement context) {
        OMTYamlDelegate delegate = getDelegate(element);
        return delegate instanceof OMTYamlVariableDelegate;
    }
}
