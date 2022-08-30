package com.misset.opp.omt;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.lang.ImportOptimizer;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class OMTImportOptimizer implements ImportOptimizer {
    public static LocalQuickFix asQuickfix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Optimize imports";
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                PsiFile file = descriptor.getPsiElement().getContainingFile();
                OMTImportOptimizer omtImportOptimizer = new OMTImportOptimizer();
                Runnable runnable = ReadAction.compute(() -> omtImportOptimizer.processFile(file));
                WriteCommandAction.runWriteCommandAction(file.getProject(), runnable);
            }

            @Override
            public boolean startInWriteAction() {
                return false;
            }
        };
    }

    @Override
    public boolean supports(@NotNull PsiFile file) {
        return file instanceof OMTFile;
    }

    @Override
    public @NotNull Runnable processFile(@NotNull PsiFile file) {
        return () ->
                // the delegate can determine its own usage and makes sure that an empty import is also removed
                PsiTreeUtil.findChildrenOfType(file, YAMLSequenceItem.class)
                        .stream()
                        .map(YAMLSequenceItem::getValue)
                        .map(OMTYamlDelegateFactory::createDelegate)
                        .filter(OMTYamlImportMemberDelegate.class::isInstance)
                        .map(OMTYamlImportMemberDelegate.class::cast)
                        .filter(OMTYamlImportMemberDelegate::isUnused)
                        .forEach(OMTYamlImportMemberDelegate::delete);
    }
}
