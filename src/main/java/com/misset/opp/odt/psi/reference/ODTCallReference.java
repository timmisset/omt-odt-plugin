package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.impl.call.ODTBaseCall;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.misset.opp.odt.ODTMultiHostInjector.resolveInOMT;

public class ODTCallReference extends PsiReferenceBase<ODTBaseCall> implements PsiPolyVariantReference {
    public ODTCallReference(@NotNull ODTBaseCall element,
                            TextRange rangeInElement) {
        super(element, rangeInElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        // An ODT call is made to either an OMT Callable element such as an Activity, Procedure within the host OMT file
        // or directly to a built-in command-call or operator

        // the order to resolve the call name is:
        // - built-in
        // - host -> OMTFile.modelItem.queries || OMTFile.modelItem.commands
        // - host -> OMTFile.model
        // - host -> OMTFile.queries || OMTFile.commands
        // - host -> OMTFile.import
        return resolveInODT()
                .or(() -> resolveInOMT(myElement, OMTCallableProvider.class, myElement.getCallId(), OMTCallableProvider::getCallableMap))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return super.isReferenceTo(element);
    }

    @Override
    public @Nullable PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 0 ? null : resolveResults[0].getElement();
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        if(script == null) { return Optional.empty(); }

        return PsiTreeUtil.findChildrenOfType(script, ODTDefineStatement.class)
                .stream()
                // must have the same name
                .filter(odtDefineStatement -> odtDefineStatement.getCallId().equals(myElement.getCallId()))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(ODTDefineStatement::getDefineName)
                .map(PsiElementResolveResult::createResults);
    }
}
