package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OMTDeclaredInterfaceReference extends PsiReferenceBase.Poly<YAMLKeyValue> implements PsiPolyVariantReference {
    public OMTDeclaredInterfaceReference(@NotNull YAMLKeyValue element,
                                         @NotNull TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        YAMLKeyValue parentOfType = PsiTreeUtil.getParentOfType(myElement, YAMLKeyValue.class, true);
        if (parentOfType == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        OMTFile module = OMTModuleUtil.getModule(getElement().getProject(), parentOfType.getKeyText());
        if (module == null) {
            return ResolveResult.EMPTY_ARRAY;
        }

        String memberName = myElement.getKeyText();
        List<PsiElement> elements = Stream.of(OMTModuleUtil.getExportedMember(module, memberName),
                        OMTModuleUtil.getExportedCallable(module, memberName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return PsiElementResolveResult.createResults(elements);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return super.isReferenceTo(element);
    }
}
