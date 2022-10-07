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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiReferencedElement;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ODTCallReference extends ODTPolyReferenceBase<ODTCall> implements LocalQuickFixProvider, EmptyResolveMessageProvider {
    private static final Logger LOGGER = Logger.getInstance(ODTCallReference.class);

    public ODTCallReference(@NotNull ODTCall element,
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
                        .filter(callable -> !PsiTreeUtil.isAncestor(callable, myElement, false))
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
    public @Nullable PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        if (resolveResults.length == 1) {
            return resolveResults[0].getElement();
        }
        Arrays.sort(resolveResults, 0, resolveResults.length, this::compareByProximity);
        return resolveResults[0].getElement();
    }

    private int compareByProximity(ResolveResult result1, ResolveResult result2) {
        PsiElement element1 = result1.getElement();
        PsiElement element2 = result2.getElement();
        if (element1 == null || element2 == null) {
            return 0;
        }
        PsiFile containingFile = myElement.getContainingFile();
        if (element1.getContainingFile() == containingFile && element2.getContainingFile() != containingFile) {
            return -1;
        } else if (element1.getContainingFile() != containingFile && element2.getContainingFile() == containingFile) {
            return 1;
        } else {
            PsiElement commonParent1 = PsiTreeUtil.findCommonParent(element1, myElement);
            PsiElement commonParent2 = PsiTreeUtil.findCommonParent(element2, myElement);
            if (commonParent1 == null && commonParent2 == null) {
                return 0;
            }
            if (commonParent1 == null) {
                return 1;
            }
            if (commonParent2 == null) {
                return -1;
            }
            return Integer.compare(
                    PsiTreeUtil.getDepth(commonParent1, commonParent1.getContainingFile()),
                    PsiTreeUtil.getDepth(commonParent2, commonParent2.getContainingFile()));
        }
    }

    @Override
    public LocalQuickFix @Nullable [] getQuickFixes() {
        ODTCall call = getElement();
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
        boolean resolveToFinalElement = !(element instanceof PsiReferencedElement);
        return Optional.ofNullable(resolve(true, resolveToFinalElement))
                .map(element.getOriginalElement()::equals)
                .orElse(false);
    }
}
