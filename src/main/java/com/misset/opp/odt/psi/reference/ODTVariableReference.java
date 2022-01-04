package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTVariableReference extends ODTPolyReferenceBase<ODTVariable> {
    private static final Key<CachedValue<ResolveResult[]>> RESULT = new Key<>("RESULT");
    private static final Key<ODTVariableReference> REFERENCE = new Key<>("REFERENCE");
    Logger LOGGER = Logger.getInstance(ODTVariableReference.class);

    public static ODTVariableReference forVariable(ODTVariable variable) {
        if (!REFERENCE.isIn(variable)) {
            ODTVariableReference odtVariableReference = new ODTVariableReference(variable);
            REFERENCE.set(variable, odtVariableReference);
            return odtVariableReference;
        }
        return REFERENCE.get(variable);
    }

    private ODTVariableReference(@NotNull ODTVariable element) {
        super(element, TextRange.allOf(element.getText()), false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "Resolving ODTVariableReference for " + getElement().getName(),
                this::getCachedResultOrResolve);
    }

    private ResolveResult @NotNull [] getCachedResultOrResolve() {
        return CachedValuesManager.getCachedValue(getElement(), RESULT, () -> {
            ResolveResult[] calculate = calculate();
            return getElement().getContainingFile().getCachedValue(calculate);
        });
    }

    private ResolveResult @NotNull [] calculate() {
        if (!getElement().isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return resolveInODT()
                .or(this::resolveFromProvider)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveFromProvider() {
        return getElement().getContainingFile()
                .resolveInOMT(OMTVariableProvider.class,
                        OMTVariableProvider.KEY,
                        getElement().getName(),
                        OMTVariableProvider::getVariableMap)
                .map(this::toResults);
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(getElement(), ODTScript.class);
        if (script == null) {
            return Optional.empty();
        }

        return PsiTreeUtil.findChildrenOfType(script, ODTVariable.class)
                .stream()
                .filter(variable -> variable.canBeDeclaredVariable(getElement()))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return getElement().setName(newElementName);
    }

    // override required to allow caching mechanism
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ODTVariableReference &&
                ((ODTVariableReference) obj).getElement().equals(getElement());
    }
}
