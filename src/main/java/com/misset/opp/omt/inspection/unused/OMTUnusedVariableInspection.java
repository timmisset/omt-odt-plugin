package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlBindingParameterDelegate;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlVariableDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class OMTUnusedVariableInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof YAMLPsiElement) || !(element.getContainingFile() instanceof OMTFile)) {
                    return;
                }
                final OMTYamlDelegate delegate = OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
                if (delegate instanceof OMTYamlVariableDelegate) {
                    OMTYamlVariableDelegate variableDelegate = (OMTYamlVariableDelegate) delegate;
                    if ((variableDelegate).isUnused()) {
                        String warning = delegate.getName() + " is never used.";
                        if (delegate instanceof OMTYamlBindingParameterDelegate) {
                            warning += " When removing this parameter, you must also remove it from the Typescript component and all " +
                                    "HTML instances of the component.";
                        }
                        holder.registerProblem(element,
                                warning,
                                LIKE_UNUSED_SYMBOL,
                                OMTRemoveQuickFix.getRemoveLocalQuickFix((variableDelegate).getSource()));
                    }
                }
            }
        };
    }
}
