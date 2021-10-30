package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.misset.opp.odt.ODTMultiHostInjector.resolveInOMT;

public class ODTVariableReference extends PsiReferenceBase.Poly<ODTVariable> implements PsiPolyVariantReference {
    public ODTVariableReference(@NotNull ODTVariable element) {
        super(element, TextRange.allOf(element.getText()), false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        if (!myElement.isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return resolveInODT()
                .or(() -> resolveInOMT(myElement, OMTVariableProvider.class, myElement.getName(), OMTVariableProvider::getVariableMap))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        if(script == null) { return Optional.empty(); }

        return PsiTreeUtil.findChildrenOfType(script, ODTVariable.class)
                .stream()
                // must have the same name
                .filter(variable -> variable.isDeclaredVariable() && variable.canBeDeclaredVariable(myElement))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
