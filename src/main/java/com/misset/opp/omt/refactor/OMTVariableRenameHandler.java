package com.misset.opp.omt.refactor;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.PsiElementRenameHandler;
import com.intellij.refactoring.rename.RenameHandler;
import com.misset.opp.omt.OMTLanguage;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * Since there is no default handler that can refactor-rename the YamlPlainTextImpl that carries
 * the variable in the OMT language. We need to create a specific handler so that the framework
 * knows that the refactor-rename should be available at the given caret position
 */
public class OMTVariableRenameHandler extends PsiElementRenameHandler implements RenameHandler {
    @Override
    public boolean isAvailableOnDataContext(@NotNull DataContext dataContext) {
        PsiFile file = (PsiFile) dataContext.getData("psi.File");
        if (file == null || file.getLanguage() != OMTLanguage.INSTANCE) {
            return false;
        }

        Caret caret = (Caret) dataContext.getData("caret");
        if (caret == null) {
            return false;
        }
        final PsiElement elementAt = file.findElementAt(caret.getOffset());
        final YAMLPlainTextImpl yamlPlainText = PsiTreeUtil.getParentOfType(elementAt, YAMLPlainTextImpl.class);
        if (yamlPlainText == null) {
            return false;
        }

        final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = OMTMetaTypeProvider.getInstance(yamlPlainText.getProject())
                .getMetaTypeProxy(yamlPlainText);
        return metaTypeProxy != null && metaTypeProxy.getMetaType() instanceof OMTNamedVariableMetaType;
    }
}
