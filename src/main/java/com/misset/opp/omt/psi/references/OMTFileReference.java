package com.misset.opp.omt.psi.references;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.util.ImportUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * Reference to other files from OMT PlainText, for example the schema of a Loadable
 * For import: references, use OMTImportPathReference
 */
public class OMTFileReference extends OMTPlainTextReference {
    public OMTFileReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String textValue = myElement.getTextValue();
        VirtualFile virtualFile = myElement.getContainingFile().getVirtualFile();
        if (virtualFile == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        VirtualFile fileByRelativePath = virtualFile.getParent().findFileByRelativePath(textValue);
        if (fileByRelativePath == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        PsiFile file = PsiManager.getInstance(myElement.getProject()).findFile(fileByRelativePath);
        if (file != null && file.isValid()) {
            return PsiElementResolveResult.createResults(file);
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        // the json file is moved, the reference should be updated to the new relative position
        String path = ImportUtil.getPathToFile(myElement, element);
        if (path != null) {
            return myElement.updateText(path);
        }
        return element;
    }
}
