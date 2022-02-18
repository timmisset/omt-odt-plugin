package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ODTCallReference extends ODTPolyReferenceBase<ODTResolvableCall> implements LocalQuickFixProvider, EmptyResolveMessageProvider {
    private static final Logger LOGGER = Logger.getInstance(ODTCallReference.class);

    public ODTCallReference(@NotNull ODTResolvableCall element,
                            TextRange rangeInElement) {
        super(element, rangeInElement, false);
    }

    public PsiElement resolve(boolean resolveToOriginalElement) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving ODTCallReference " + myElement.getCallId(), () -> {
            ResolveResult[] resolveResults = multiResolveToOriginal(resolveToOriginalElement);
            return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
        });
    }

    public ResolveResult @NotNull [] multiResolveToOriginal(boolean resolveToOriginalElement) {
        // An ODT call is made to either an OMT Callable element such as an Activity, Procedure within the host OMT file
        // or directly to a built-in command-call or operator

        // the order to resolve the call name is:
        // - built-in
        // - host -> OMTFile.modelItem.queries || OMTFile.modelItem.commands
        // - host -> OMTFile.model
        // - host -> OMTFile.queries || OMTFile.commands
        // - host -> OMTFile.import
        return resolveInODT(resolveToOriginalElement)
                .or(() -> resolveFromProvider(resolveToOriginalElement))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveFromProvider(boolean resolveToOriginalElement) {
        Optional<List<PsiCallable>> psiElements = myElement.getODTFile()
                .resolveInOMT(OMTCallableProvider.class,
                        OMTCallableProvider.KEY,
                        myElement.getCallId(),
                        (provider, mapping) -> provider.getCallableMap(mapping, myElement.getODTFile().getHost()));

        return psiElements.map(psiCallables -> toResults(psiCallables, resolveToOriginalElement));
    }

    private Optional<ResolveResult[]> resolveInODT(boolean resolveToOriginalElement) {
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
                .map(odtDefineStatement -> resolveToOriginalElement ? odtDefineStatement.getOriginalElement() : odtDefineStatement)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolveToOriginal(true);
    }

    @Override
    public LocalQuickFix @Nullable [] getQuickFixes() {
        ODTResolvableCall call = getElement();
        ODTFile containingFile = call.getODTFile();
        if (containingFile == null) {
            return LocalQuickFix.EMPTY_ARRAY;
        }

        OMTFile hostFile = containingFile.getHostFile();
        List<LocalQuickFix> importQuickFixes = new ArrayList<>();
        if (hostFile != null) {
            // inside an OMT file, check for possible imports:
            importQuickFixes.addAll(OMTImportUtil.getImportQuickFixes(hostFile, call));
        }

        // todo:
        // create new ODT Command / Query
        return importQuickFixes.toArray(LocalQuickFix[]::new);
    }

    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        return "Cannot resolve call '" + getElement().getName() + "'";
    }
}
