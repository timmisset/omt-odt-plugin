package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTExportMemberMetaType;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.module.OMTDeclaredInterfaceMetaType;
import com.misset.opp.omt.meta.module.OMTDeclaredModuleMetaType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import com.misset.opp.omt.meta.scalars.OMTOntologyPrefixMetaType;
import com.misset.opp.omt.meta.scalars.OMTParamTypeType;
import com.misset.opp.omt.psi.impl.delegate.keyvalue.*;
import com.misset.opp.omt.psi.impl.delegate.plaintext.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * The OMTYamlDelegateFactory creates delegates that are registered as UserData in the original PsiElements
 * The delegates should themselves not be used for actual navigation, Lookups etc.
 * The OMTMetaType is used to determine which kind of delegate should be created.
 * <p>
 * In case there is no delegate compatible with the OMTMetaType a placeholder 'NOT_A_DELEGATE' is returned instead
 */
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
        OMTYamlDelegate delegate = null;
        if (psiElement instanceof YAMLKeyValue) {
            delegate = createKeyValueDelegate((YAMLKeyValue) psiElement);
        } else if (psiElement instanceof YAMLPlainTextImpl) {
            delegate = createPlainTextDelegate((YAMLPlainTextImpl) psiElement);
        }
        if (delegate == null) {
            delegate = new NOT_A_DELEGATE(psiElement.getNode());
        }

        DELEGATE.set(psiElement, delegate);
        return delegate;
    }

    private static OMTYamlDelegate createKeyValueDelegate(YAMLKeyValue keyValue) {
        final OMTMetaTypeProvider instance = OMTMetaTypeProvider.getInstance(keyValue.getProject());
        final YamlMetaType valueMetaType = instance.getResolvedKeyValueMetaTypeMeta(keyValue);
        if (valueMetaType instanceof OMTModelItemMetaType) {
            return new OMTYamlModelItemDelegate(keyValue);
        } else if (valueMetaType instanceof OMTIriMetaType) {
            return new OMTYamlPrefixIriDelegate(keyValue);
        } else if (valueMetaType instanceof OMTImportMemberMetaType) {
            return new OMTYamlImportPathDelegate(keyValue);
        } else if (valueMetaType instanceof OMTDeclaredModuleMetaType) {
            return new OMTYamlDeclaredModuleDelegate(keyValue);
        } else if (valueMetaType instanceof OMTDeclaredInterfaceMetaType) {
            return new OMTYamlDeclaredInterfaceDelegate(keyValue);
        }
        return null;
    }

    private static OMTYamlDelegate createPlainTextDelegate(YAMLPlainTextImpl yamlPlainText) {
        final OMTMetaTypeProvider instance = OMTMetaTypeProvider.getInstance(yamlPlainText.getProject());
        final YamlMetaType metaType = instance.getResolvedMetaType(yamlPlainText);
        if (metaType instanceof OMTParamMetaType) {
            return new OMTYamlParameterDelegate(yamlPlainText);
        } else if (metaType instanceof OMTNamedVariableMetaType) {
            return new OMTYamlVariableDelegate(yamlPlainText);
        } else if (metaType instanceof OMTImportMemberMetaType) {
            return new OMTYamlImportMemberDelegate(yamlPlainText);
        } else if (metaType instanceof OMTExportMemberMetaType) {
            return new OMTYamlPayloadQueryReferenceDelegate(yamlPlainText);
        } else if (metaType instanceof OMTOntologyPrefixMetaType) {
            return new OMTYamlOntologyPrefixDelegate(yamlPlainText);
        } else if (metaType instanceof OMTParamTypeType) {
            return new OMTYamlParamTypeDelegate(yamlPlainText);
        }
        return null;
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
