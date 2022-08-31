package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.ReferencedElement;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Collectors;

public class ODTCallReference extends ODTPolyReferenceBase<ODTResolvableCall> implements LocalQuickFixProvider, EmptyResolveMessageProvider {
    private static final Logger LOGGER = Logger.getInstance(ODTCallReference.class);

    public ODTCallReference(@NotNull ODTResolvableCall element,
                            TextRange rangeInElement) {
        super(element, rangeInElement, false);
    }

    public PsiElement resolve(boolean resolveToOriginalElement) {
        return resolve(resolveToOriginalElement, true);
    }

    public PsiElement resolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving ODTCallReference " + myElement.getCallId(), () -> {
            ResolveResult[] resolveResults = multiResolve(resolveToOriginalElement, resolveToFinalElement);
            return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
        });
    }

    public ResolveResult @NotNull [] multiResolve(boolean resolveToOriginalElement,
                                                  boolean resolveToFinalElement) {
        // An ODT call is made to either an OMT Callable element such as an Activity, Procedure within the host OMT file
        // or directly to a built-in command-call or operator

        // the order to resolve the call name is:
        // - built-in
        // - host -> OMTFile.modelItem.queries || OMTFile.modelItem.commands
        // - host -> OMTFile.model
        // - host -> OMTFile.queries || OMTFile.commands
        // - host -> OMTFile.import
        PsiFile containingFile = myElement.getContainingFile();
        if (!(containingFile instanceof ODTFile)) {
            return ResolveResult.EMPTY_ARRAY;
        }
        ODTFile file = (ODTFile) containingFile;
        return toResults(file.getCallables(myElement.getCallId())
                        .stream()
                        .filter(PsiCallable.class::isInstance)
                        .map(PsiCallable.class::cast)
                        .filter(psiCallable -> file.isAccessible(myElement, psiCallable))
                        .collect(Collectors.toList())
                , resolveToOriginalElement, resolveToFinalElement);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolve(true, true);
    }

    @Override
    public LocalQuickFix @Nullable [] getQuickFixes() {
        ODTResolvableCall call = getElement();
        PsiFile containingFile = call.getContainingFile();
        if (!(containingFile instanceof ODTFile)) {
            return LocalQuickFix.EMPTY_ARRAY;
        }
        return ((ODTFile) containingFile).getRegisterImportQuickfixes(call).toArray(LocalQuickFix[]::new);
    }

    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        return "Cannot resolve call '" + getElement().getName() + "'";
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        boolean resolveToFinalElement = !(element instanceof ReferencedElement);
        return Optional.ofNullable(resolve(true, resolveToFinalElement))
                .map(element.getOriginalElement()::equals)
                .orElse(false);
    }
}
