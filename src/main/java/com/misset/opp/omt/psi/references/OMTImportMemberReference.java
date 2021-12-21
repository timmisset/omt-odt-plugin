package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OMTImportMemberReference extends OMTPlainTextReference {

    public OMTImportMemberReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    public ResolveResult @NotNull [] multiResolveToOriginal(boolean resolveToOriginalElement) {
        final YAMLPlainTextImpl element = getElement();
        final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);

        String name = element.getText();
        final HashMap<String, List<PsiCallable>> exportingMembersMap = OMTImportMetaType.getExportedMembersFromOMTFile(
                keyValue);
        return Optional.ofNullable(exportingMembersMap.get(name))
                .or(() -> Optional.ofNullable(exportingMembersMap.get("@" + name)))
                .map(psiCallables -> toResults(psiCallables, resolveToOriginalElement))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    public PsiElement resolve(boolean resolveToOriginalElement) {
        ResolveResult[] resolveResults = multiResolveToOriginal(resolveToOriginalElement);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolveToOriginal(true);
    }
}
