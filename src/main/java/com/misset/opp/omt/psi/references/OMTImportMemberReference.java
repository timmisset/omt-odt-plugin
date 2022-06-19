package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;

/**
 * Reference for an imported member
 * There are 3 possible elements the reference can resolve to based on the following situation:
 * <p>
 * originalFile.omt
 * model:
 * MyActivity: !Activity
 * <p>
 * index.omt
 * imports:
 * originalFile.omt:
 * - MyActivity
 * <p>
 * importing.omt
 * imports:
 * index.omt:
 * - MyActivity <-- reference
 * <p>
 * In reference would resolve to the imported MyActivity member of index.omt which subsequently would resolve to
 * the actual !Activity.
 * <p>
 * In order to resolve to the !Activity, use resolveToFinalElement, otherwise it will resolve to the first resolvable
 * element (the imported member in index.omt)
 * Since the YamlKeyValue is wrapped into the OMTCallable it is not actually part of the PsiTree and thus the resolveToOriginalElement
 * is required to resolve to the actual PsiElement
 * <p>
 * To know if an import is used, call resolve without resolving to the final element on the references
 * To validate calls made to the members resolve without resolveing to the orginal element to use the Wrapper
 */
public class OMTImportMemberReference extends OMTPlainTextReference {

    public OMTImportMemberReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    public ResolveResult @NotNull [] multiResolveToOriginal(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        final YAMLPlainTextImpl element = getElement();
        final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);
        if (keyValue == null) {
            return ResolveResult.EMPTY_ARRAY;
        }

        String name = element.getText();
        final HashMap<String, List<PsiCallable>> exportingMembersMap = OMTImportMetaType.getInstance().getExportedMembersFromOMTFile(
                keyValue);
        return fromExportableMembersMap(exportingMembersMap, name, resolveToOriginalElement, resolveToFinalElement);
    }

    public PsiElement resolve(boolean resolveToOriginalElement) {
        return resolve(resolveToOriginalElement, true);
    }

    public PsiElement resolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        ResolveResult[] resolveResults = multiResolveToOriginal(resolveToOriginalElement, resolveToFinalElement);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolveToOriginal(true, true);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element.getOriginalElement() == myElement.getOriginalElement()) {
            return false;
        }
        boolean resolveToFinalElement = !(element instanceof OMTYamlImportMemberDelegate);
        PsiElement resolve = resolve(true, resolveToFinalElement);
        return resolve == element.getOriginalElement();
    }
}
