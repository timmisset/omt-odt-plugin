package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTImportMetaType;
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

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final YAMLPlainTextImpl element = getElement();
        final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);

        String name = element.getText();
        final HashMap<String, List<PsiElement>> exportingMembersMap = OMTImportMetaType.getExportedMembersFromOMTFile(
                keyValue);
        return Optional.ofNullable(exportingMembersMap.get(name))
                .or(() -> Optional.ofNullable(exportingMembersMap.get("@" + name)))
                .map(psiElements -> psiElements.get(0))
                .map(PsiElementResolveResult::createResults)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
