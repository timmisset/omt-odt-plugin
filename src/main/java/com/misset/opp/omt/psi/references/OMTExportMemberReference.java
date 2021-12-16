package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Objects;

public class OMTExportMemberReference extends OMTPlainTextReference {

    public OMTExportMemberReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final YAMLPlainTextImpl element = getElement();
        final YAMLMapping mapping = PsiTreeUtil.getTopmostParentOfType(element, YAMLMapping.class);

        if (mapping != null) {
            YAMLKeyValue imports = mapping.getKeyValueByKey("import");
            if (imports == null) {
                return ResolveResult.EMPTY_ARRAY;
            }
            return PsiTreeUtil.findChildrenOfType(imports, YAMLPlainTextImpl.class)
                    .stream()
                    .filter(plainText -> plainText.getText().equals(element.getText()))
                    .map(YAMLScalarImpl::getReference)
                    .filter(Objects::nonNull)
                    .map(PsiReference::resolve)
                    .filter(Objects::nonNull)
                    .map(PsiElementResolveResult::new)
                    .toArray(PsiElementResolveResult[]::new);
        }
        return ResolveResult.EMPTY_ARRAY;
    }
}
