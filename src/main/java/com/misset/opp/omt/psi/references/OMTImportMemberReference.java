package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OMTImportMemberReference extends PsiReferenceBase<YAMLPlainTextImpl> implements PsiPolyVariantReference {

    public OMTImportMemberReference(@NotNull YAMLPlainTextImpl element) {
        super(element, TextRange.allOf(element.getText()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final YAMLPlainTextImpl element = getElement();
        final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);
        final OMTFile omtFile = OMTImportMetaType.resolveToOMTFile(keyValue);
        if(omtFile != null) {
            String name = element.getText();
            final HashMap<String, List<PsiElement>> exportingMembersMap = omtFile.getExportingMembersMap();
            return Optional.ofNullable(exportingMembersMap.get(name))
                    .or(() -> Optional.ofNullable(exportingMembersMap.get("@" + name)))
                    .map(psiElements -> psiElements.get(0))
                    .map(PsiElementResolveResult::createResults)
                    .orElse(ResolveResult.EMPTY_ARRAY);
        }
        return new ResolveResult[0];
    }

    @Override
    public @Nullable PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }
}
