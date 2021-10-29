package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.ex.temp.TempFileSystem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public class OMTImportPathReference extends PsiReferenceBase.Poly<YAMLKeyValue> implements PsiPolyVariantReference {

    public OMTImportPathReference(@NotNull YAMLKeyValue element,
                                  @NotNull TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final OMTFile omtFile = OMTImportMetaType.resolveToOMTFile(myElement);
        if (omtFile != null) {
            return PsiElementResolveResult.createResults(omtFile);
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        String path = getPathToFile(myElement, element);
        if(path != null) {
            return myElement.setName(path);
        }
        return element;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        final Optional<String> oldName = Optional.ofNullable(resolve())
                .map(PsiElement::getContainingFile)
                .map(PsiFileSystemItem::getName);
        if(oldName.isPresent()) {
            return  myElement.setName(myElement.getKeyText().replace(oldName.get(), newElementName));
        }
        return myElement;
    }

    @Nullable
    public static String getPathToFile(PsiElement from,
                                       PsiElement to) {
        PsiFile fromFile = from instanceof PsiFile ? (PsiFile) from : from.getContainingFile();
        PsiFile toFile = to instanceof PsiFile ? (PsiFile) to : to.getContainingFile();
        if (fromFile == null || toFile == null) {
            return null;
        }
        if(fromFile.getVirtualFile().getFileSystem() instanceof TempFileSystem) {
            return "./unit-test-success/myFile.omt";
        }

        final String pathToFile = fromFile.getVirtualFile().getParent().toNioPath()
                .relativize(toFile.getVirtualFile().toNioPath())
                .toString()
                .replace("\\", "/");
        return (!pathToFile.startsWith(".") ? "./" : "") + pathToFile;
    }
}
