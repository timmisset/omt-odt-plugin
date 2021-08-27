package com.misset.opp.odt.psi.references;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.wrapping.impl.ODTBaseVariable;
import com.misset.opp.odt.psi.wrapping.impl.ODTDefinedVariableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ODTVariableReference extends PsiReferenceBase<ODTBaseVariable> implements PsiPolyVariantReference {
    public ODTVariableReference(@NotNull ODTBaseVariable element) {
        super(element, TextRange.create(0, element.getTextLength()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        if (!myElement.isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return Optional.ofNullable(resolveInODT())
                .orElse(resolveInOMT());
    }
    private ResolveResult[] resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(myElement, ODTScript.class);
        if(script == null) { return ResolveResult.EMPTY_ARRAY; }

        return PsiTreeUtil.findChildrenOfType(script, ODTDefinedVariableImpl.class)
                .stream()
                // must have the same name
                .filter(variable -> variable.canBeDefinedVariable(myElement))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults)
                .orElse(null);
    }
    private ResolveResult[] resolveInOMT() {
        final InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(myElement.getProject());
        final PsiLanguageInjectionHost injectionHost = languageManager.getInjectionHost(myElement);
        if(injectionHost == null) { return ResolveResult.EMPTY_ARRAY; }


        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public @Nullable PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 0 ? null : resolveResults[0].getElement();
    }
}
