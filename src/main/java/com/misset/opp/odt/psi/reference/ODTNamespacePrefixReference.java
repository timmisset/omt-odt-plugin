package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.misset.opp.odt.ODTMultiHostInjector.resolveInOMT;

public class ODTNamespacePrefixReference extends PsiReferenceBase<ODTNamespacePrefix> implements PsiPolyVariantReference {
    public ODTNamespacePrefixReference(@NotNull ODTNamespacePrefixImpl element) {
        super(element, TextRange.allOf(element.getName()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        if (!myElement.isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        // resolve in current ODT file
        // then resolve in OMT using the PrefixProviders
        return resolveInODT()
                .or(() -> resolveInOMT(myElement, OMTPrefixProvider.class, myElement.getName(), OMTPrefixProvider::getPrefixMap))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        if (script == null) {
            return Optional.empty();
        }

        return PsiTreeUtil.findChildrenOfType(script, ODTDefinePrefix.class)
                .stream()
                // must have the same name
                .filter(prefix -> Optional.ofNullable(getElement().getName())
                        .map(s -> s.equals(prefix.getNamespacePrefix().getName()))
                        .orElse(false))
                .map(ODTDefinePrefix::getNamespacePrefix)
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public @Nullable PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 0 ? null : resolveResults[0].getElement();
    }
}
