package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.ImportUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public class OMTImportPathReference extends PsiReferenceBase.Poly<YAMLKeyValue> implements PsiPolyVariantReference {

    public OMTImportPathReference(@NotNull YAMLKeyValue element,
                                  @NotNull TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final OMTFile omtFile = OMTImportMetaType.getInstance().resolveToOMTFile(myElement);
        if (omtFile != null) {
            return PsiElementResolveResult.createResults(omtFile);
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        String path = ImportUtil.getPathToFile(myElement, element);
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

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return super.isReferenceTo(element);
    }
}
