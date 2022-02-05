package com.misset.opp.odt.inspection.quikfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;

public class ODTImportMemberLocalQuickFix implements LocalQuickFix {
    private final String importPath;
    private final String memberName;
    private final String type;

    public ODTImportMemberLocalQuickFix(OMTFile omtFile, PsiCallable callable) {
        importPath = OMTImportUtil.getImportPath(omtFile, callable);
        memberName = callable.getName();
        type = callable.getType();
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return "Import";
    }

    @Override
    public @IntentionName @NotNull String getName() {
        return "Import as " + type + " from " + importPath;
    }


    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement psiElement = descriptor.getPsiElement();
        OMTFile importingFile = OMTImportUtil.getImportedFile(psiElement.getContainingFile());
        if (importingFile == null) {
            return;
        }
        OMTImportUtil.addImport(importingFile, project, importPath, memberName);
    }
}
