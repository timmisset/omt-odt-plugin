package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTIriMetaType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlDelegateFactory {
    private static final Key<OMTYamlDelegate> DELEGATE = new Key<>("OMT_YAML_DELEGATE");

    private OMTYamlDelegateFactory() {

    }

    public static OMTYamlDelegate createDelegate(YAMLPsiElement psiElement) {
        final OMTYamlDelegate omtYamlDelegate = DELEGATE.get(psiElement);
        if (omtYamlDelegate != null) {
            return omtYamlDelegate;
        }

        // create delegate by meta-type information:
        final OMTMetaTypeProvider instance = OMTMetaTypeProvider.getInstance(psiElement.getProject());
        OMTYamlDelegate delegate = null;
        if (psiElement instanceof YAMLKeyValue) {
            final YAMLKeyValue keyValue = (YAMLKeyValue) psiElement;
            final YamlMetaType resolvedKeyValueMetaTypeMeta = instance.getResolvedKeyValueMetaTypeMeta(keyValue);
            if (resolvedKeyValueMetaTypeMeta instanceof OMTModelItemMetaType) {
                delegate = new OMTYamlModelItemDelegate(keyValue);
            } else if (resolvedKeyValueMetaTypeMeta instanceof OMTIriMetaType) {
                delegate = new OMTYamlPrefixIriDelegate(keyValue);
            }
        } else if (psiElement instanceof YAMLPlainTextImpl) {
            final YamlMetaType metaType = instance.getResolvedMetaType(psiElement);
            final YAMLValue yamlValue = (YAMLValue) psiElement;
            if (metaType instanceof OMTNamedVariableMetaType) {
                return new OMTYamlVariableDelegate(yamlValue);
            } else if (metaType instanceof OMTImportMemberMetaType) {
                return new OMTImportMemberDelegate(yamlValue);
            }
        }
        if (delegate == null) {
            delegate = new NOT_A_DELEGATE(psiElement.getNode());
        }

        DELEGATE.set(psiElement, delegate);
        return delegate;
    }

    private static class NOT_A_DELEGATE extends ASTWrapperPsiElement implements OMTYamlDelegate {

        public NOT_A_DELEGATE(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
            return null;
        }
    }
}