package com.misset.opp.omt.intention;

import com.intellij.codeInspection.HintAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;

import static com.misset.opp.omt.util.OMTImportUtil.addImport;

public class ImportMemberIntention implements HintAction {
    private final PsiCallable callable;
    private final String importPath;

    public ImportMemberIntention(OMTFile omtFile, PsiCallable callable) {
        this.callable = callable;
        importPath = OMTImportUtil.getImportPath(omtFile, callable);
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "Import as " + callable.getType() + " from " + importPath;
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Import";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        OMTFile importedFile = OMTImportUtil.getImportedFile(file);
        if (importedFile == null) {
            return;
        }
        addImport(importedFile, project, editor, importPath, callable.getName());
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }

    @Override
    public boolean showHint(@NotNull Editor editor) {
        return true;
    }

    @Override
    public boolean fixSilently(@NotNull Editor editor) {
        return HintAction.super.fixSilently(editor);
    }
}
