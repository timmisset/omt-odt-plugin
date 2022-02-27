package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class OMTPlainTextReference extends PsiReferenceBase.Poly<YAMLPlainTextImpl> implements PsiPolyVariantReference {
    protected TextRange textRange;

    public OMTPlainTextReference(@NotNull YAMLPlainTextImpl element) {
        this(element, TextRange.allOf(element.getText()));
    }

    public OMTPlainTextReference(@NotNull YAMLPlainTextImpl element,
                                 TextRange textRange) {
        super(element, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return handleElementRename(newElementName, textRange);
    }

    protected PsiElement handleElementRename(@NotNull String newElementName,
                                             TextRange textRange) {
        return Optional.ofNullable(YAMLElementGenerator.getInstance(myElement.getProject()))
                .map(generator -> generator.createYamlKeyValue("foo",
                        textRange.replace(myElement.getText(), newElementName)))
                .map(YAMLKeyValue::getValue)
                .map(myElement::replace)
                .orElse(myElement);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements, boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        return resolvedElements.stream()
                .filter(Objects::nonNull)
                .map(psiCallable -> {
                    PsiElement element = psiCallable;
                    if (element instanceof OMTYamlImportMemberDelegate && resolveToFinalElement) {
                        element = ((OMTYamlImportMemberDelegate) element).getFinalElement();
                    }
                    if (element == null) {
                        return null;
                    }
                    return resolveToOriginalElement ? element.getOriginalElement() : element;
                })
                .filter(Objects::nonNull)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements) {
        return toResults(resolvedElements, true, true);
    }

    protected ResolveResult[] resolveExportableMemberReference() {
        return resolveExportableMemberReference(true);
    }

    /**
     * Resolve a reference that can be found anywhere within the containing file
     * This can be an imported member or an exportable member which is declared here
     */
    protected ResolveResult[] resolveExportableMemberReference(boolean resolveToFinalElement) {
        YAMLPlainTextImpl element = getElement();
        PsiFile containingFile = element.getContainingFile();
        if (!(containingFile instanceof OMTFile)) {
            return ResolveResult.EMPTY_ARRAY;
        } else {
            HashMap<String, List<PsiCallable>> exportingMembersMap = ((OMTFile) containingFile).getExportingMembersMap();
            String name = element.getName();
            return fromExportableMembersMap(exportingMembersMap, name, true, resolveToFinalElement);
        }
    }

    protected ResolveResult[] fromExportableMembersMap(HashMap<String, List<PsiCallable>> map, String name, boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        return Optional.ofNullable(map.get(name))
                .map(psiCallables -> toResults(psiCallables, resolveToOriginalElement, resolveToFinalElement))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
