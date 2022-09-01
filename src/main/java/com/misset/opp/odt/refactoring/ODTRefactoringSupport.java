package com.misset.opp.odt.refactoring;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.changeSignature.ChangeSignatureHandler;
import com.misset.opp.odt.intentions.IntroduceLocalVariableIntention;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTRefactoringSupport extends RefactoringSupportProvider {
    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        return element instanceof SupportsSafeDelete && ((SupportsSafeDelete) element).isUnused();
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceVariableHandler() {
        return new IntroduceLocalVariableIntention();
    }

    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
        return element instanceof ODTVariable;
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceVariableHandler(PsiElement element) {
        return super.getIntroduceVariableHandler(element);
    }

    @Override
    public @Nullable RefactoringActionHandler getExtractMethodHandler() {
        return super.getExtractMethodHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceConstantHandler() {
        return super.getIntroduceConstantHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceFieldHandler() {
        return super.getIntroduceFieldHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceParameterHandler() {
        return super.getIntroduceParameterHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceFunctionalParameterHandler() {
        return super.getIntroduceFunctionalParameterHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getPullUpHandler() {
        return super.getPullUpHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getPushDownHandler() {
        return super.getPushDownHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getExtractInterfaceHandler() {
        return super.getExtractInterfaceHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getExtractModuleHandler() {
        return super.getExtractModuleHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getExtractSuperClassHandler() {
        return super.getExtractSuperClassHandler();
    }

    @Override
    public @Nullable ChangeSignatureHandler getChangeSignatureHandler() {
        return super.getChangeSignatureHandler();
    }

    @Override
    public @Nullable RefactoringActionHandler getExtractClassHandler() {
        return super.getExtractClassHandler();
    }
}
