package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.util.OMTModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public class OMTModuleReference extends PsiReferenceBase.Poly<YAMLKeyValue> implements PsiPolyVariantReference {
    public OMTModuleReference(@NotNull YAMLKeyValue element,
                              @NotNull TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return Optional.of(myElement.getKeyText())
                .map(moduleName -> OMTModuleUtil.getModule(getElement().getProject(), moduleName))
                .map(PsiElementResolveResult::new)
                .stream()
                .toArray(ResolveResult[]::new);
    }
}
