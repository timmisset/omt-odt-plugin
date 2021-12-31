package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class OMTUnusedImportMemberInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof YAMLPsiElement)) {
                    return;
                }
                final OMTYamlDelegate delegate = OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
                if (delegate instanceof OMTYamlImportMemberDelegate) {
                    if (((OMTYamlImportMemberDelegate) delegate).isUnused()) {
                        holder.registerProblem(element,
                                "Import for " + element.getText() + " is never used",
                                LIKE_UNUSED_SYMBOL,
                                OMTRemoveQuickFix.getRemoveLocalQuickFix("import member"));
                    }
                }
            }
        };
    }
}
