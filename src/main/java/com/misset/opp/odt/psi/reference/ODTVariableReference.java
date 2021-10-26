package com.misset.opp.odt.psi.reference;

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
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.misset.opp.omt.meta.OMTMetaTreeUtil.collectMetaParents;

public class ODTVariableReference extends PsiReferenceBase<ODTVariable> implements PsiPolyVariantReference {
    public ODTVariableReference(@NotNull ODTVariable element) {
        super(element, TextRange.allOf(element.getText()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        if (!myElement.isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return resolveInODT()
                .or(this::resolveInOMT)
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
    private Optional<ResolveResult[]> resolveInOMT() {
        final InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(myElement.getProject());
        final PsiLanguageInjectionHost injectionHost = languageManager.getInjectionHost(myElement);
        if(injectionHost == null) { return Optional.empty(); }

        final LinkedHashMap<YAMLMapping, OMTVariableProvider> linkedHashMap = collectMetaParents(
                injectionHost,
                YAMLMapping.class,
                OMTVariableProvider.class,
                false,
                Objects::isNull);
        for(YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTVariableProvider variableProvider = linkedHashMap.get(mapping);
            final HashMap<String, List<PsiElement>> variableMap = variableProvider.getVariableMap(mapping);
            if(variableMap.containsKey(myElement.getName())) {
                final PsiElement element = variableMap.get(myElement.getName()).get(0);
                if(element == null) { return Optional.empty(); }
                return Optional.of(PsiElementResolveResult.createResults(element));
            }
        }

        return Optional.empty();
    }

    @Override
    public @Nullable PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 0 ? null : resolveResults[0].getElement();
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return super.isReferenceTo(element);
    }
}
