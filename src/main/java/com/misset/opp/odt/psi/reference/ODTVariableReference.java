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
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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
                .or(() -> myElement.getContainingFile()
                        .resolveInOMT(OMTVariableProvider.class,
                                myElement.getName(),
                                OMTVariableProvider::getVariableMap))
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        if (script == null) {
            return Optional.empty();
        }

        return PsiTreeUtil.findChildrenOfType(script, ODTVariable.class)
                .stream()
                // must have the same name
                .filter(variable -> validateCommonParent(variable, myElement) &&
                        variable.isDeclaredVariable() &&
                        variable.canBeDeclaredVariable(myElement))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    private boolean validateCommonParent(ODTVariable declared,
                                         ODTVariable usage) {
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(declared, usage);
        // DEFINE COMMAND command($variable) => { $variable } <-- same common parent as ODTDefineStatement OK
        // DEFINE COMMAND command => { VAR $variable; @LOG($variable); } <-- common parent will be script, so not OK (yet)
        if (commonParent instanceof ODTDefineStatement) {
            return true;
        }

        if (commonParent instanceof ODTScript) {
            // only when the declared variable is in the root of the common parent:
            // VAR $variable
            // IF true { @LOG($variable); } <-- passed

            // IF true { VAR $variable; } ELSE { @LOG($variable); } <-- failed
            return PsiTreeUtil.getParentOfType(declared, ODTDefineStatement.class, ODTScript.class) == commonParent;
        }
        return false;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
