package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTFile;
import org.jetbrains.annotations.NotNull;

public class ODTTypePrefixAnnotationReference extends ODTPolyReferenceBase<PsiElement> {
    final TextRange textRange;

    public ODTTypePrefixAnnotationReference(PsiElement psiElement,
                                            TextRange textRange) {
        super(psiElement, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final ODTFile containingFile = (ODTFile) myElement.getContainingFile();
        String prefix = textRange.substring(myElement.getText());
        return toResults(containingFile.getPrefixes(prefix));
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        String psiDocTag = getRangeInElement().replace(getElement().getText(), newElementName);
        return myElement.replace(ODTElementGenerator.getInstance(getElement().getProject())
                .fromFile("/**\n * " + psiDocTag + "\n */", PsiDocTag.class));
    }
}
