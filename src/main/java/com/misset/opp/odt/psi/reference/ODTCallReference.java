package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTCallReference extends PsiReferenceBase.Poly<ODTResolvableCall> implements PsiPolyVariantReference {
    public ODTCallReference(@NotNull ODTResolvableCall element,
                            TextRange rangeInElement) {
        super(element, rangeInElement, false);
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
                .or(() -> myElement.getContainingFile()
                        .resolveInOMT(OMTCallableProvider.class,
                                OMTCallableProvider.KEY,
                                myElement.getCallId(),
                                OMTCallableProvider::getCallableMap))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        ODTDefineStatement defineStatement = PsiTreeUtil.getParentOfType(myElement, ODTDefineStatement.class);
        if (script == null) {
            return Optional.empty();
        }

        return PsiTreeUtil.findChildrenOfType(script, ODTDefineStatement.class)
                .stream()
                // must have the same name and cannot contain itself
                // i.e. a call from an ODTDefineStatement must always refer to one listed above it
                .filter(odtDefineStatement -> defineStatement != odtDefineStatement &&
                        odtDefineStatement.getCallId().equals(myElement.getCallId()))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(ODTDefineStatement::getDefineName)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
