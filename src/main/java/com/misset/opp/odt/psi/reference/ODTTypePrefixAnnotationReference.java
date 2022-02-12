package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;

public class ODTTypePrefixAnnotationReference extends ODTPolyReferenceBase<PsiElement> {
    final TextRange textRange;
    Logger LOGGER = Logger.getInstance(ODTTTLSubjectPredicateReference.class);

    public ODTTypePrefixAnnotationReference(PsiElement psiElement,
                                            TextRange textRange) {
        super(psiElement, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving ODTTypePrefixAnnotationReference", () -> {
            final ODTFile containingFile = (ODTFile) myElement.getContainingFile();
            return containingFile.resolveInOMT(OMTPrefixProvider.class,
                            OMTPrefixProvider.KEY,
                            textRange.substring(myElement.getText()),
                            OMTPrefixProvider::getPrefixMap)
                    .map(this::toResults)
                    .orElse(ResolveResult.EMPTY_ARRAY);
        });
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        String psiDocTag = getRangeInElement().replace(getElement().getText(), newElementName);
        return myElement.replace(ODTElementGenerator.getInstance(getElement().getProject())
                .fromFile("/**\n * " + psiDocTag + "\n */", PsiDocTag.class));
    }
}
