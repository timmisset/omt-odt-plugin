package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.omt.OMTImportOptimizer;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class OMTUnusedImportMemberInspection extends LocalInspectionTool {

    private static final Logger LOGGER = Logger.getInstance(OMTUnusedImportMemberInspection.class);

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof YAMLPsiElement) || !(element.getContainingFile() instanceof OMTFile)) {
                    return;
                }
                final PsiNamedElement delegate = OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
                if (delegate instanceof OMTYamlImportMemberDelegate) {
                    LoggerUtil.runWithLogger(LOGGER, "Inspection of " + element.getText(), () -> {
                        if (((OMTYamlImportMemberDelegate) delegate).isUnused()) {
                            holder.registerProblem(element,
                                    "Import for " + element.getText() + " is never used",
                                    LIKE_UNUSED_SYMBOL,
                                    OMTRemoveQuickFix.getRemoveLocalQuickFix("import member"),
                                    OMTImportOptimizer.asQuickfix());
                        }
                    });
                }
            }
        };
    }
}
