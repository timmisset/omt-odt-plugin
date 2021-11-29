package com.misset.opp.omt.symbols;

import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiSymbolDeclaration;
import com.intellij.model.psi.PsiSymbolDeclarationProvider;
import com.intellij.model.psi.PsiSymbolService;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class OMTDeclarationProvider implements PsiSymbolDeclarationProvider {
    @Override
    public @NotNull Collection<? extends @NotNull PsiSymbolDeclaration> getDeclarations(@NotNull PsiElement element,
                                                                                        int offsetInElement) {

        final YamlMetaType metaType = Optional.of(element)
                .filter(YAMLValue.class::isInstance)
                .map(e -> OMTMetaTypeProvider.getInstance(e.getProject()).getValueMetaType((YAMLValue) element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
        final PsiSymbolDeclaration declaration;
        if (metaType instanceof OMTNamedVariableMetaType) {
            declaration = getDeclaration(element,
                    ((OMTNamedVariableMetaType) metaType).getNameTextRange((YAMLValue) element));
        } else {
            declaration = null;
        }
        return declaration != null ? Collections.singleton(declaration) : Collections.emptySet();
    }

    private PsiSymbolDeclaration getDeclaration(PsiElement element,
                                                TextRange textRange) {
        return new PsiSymbolDeclaration() {

            @Override
            public @NotNull PsiElement getDeclaringElement() {
                return element;
            }

            @Override
            public @NotNull TextRange getRangeInDeclaringElement() {
                return textRange;
            }

            @Override
            public @NotNull Symbol getSymbol() {
                return PsiSymbolService.getInstance().asSymbol(element);
            }
        };
    }
}
