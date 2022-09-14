package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTGraphShapeHandlerMemberMetaType;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTImportedMemberRefMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.variables.OMTBindingParameterMetaType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.module.OMTDeclaredInterfaceMetaType;
import com.misset.opp.omt.meta.module.OMTDeclaredModuleMetaType;
import com.misset.opp.omt.meta.scalars.OMTBaseParameterMetaType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import com.misset.opp.omt.meta.scalars.OMTOntologyPrefixMetaType;
import com.misset.opp.omt.meta.scalars.OMTParamTypeMetaType;
import com.misset.opp.omt.meta.scalars.references.OMTPayloadQueryReferenceMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTFileReferenceMetaType;
import com.misset.opp.omt.psi.impl.delegate.keyvalue.*;
import com.misset.opp.omt.psi.impl.delegate.plaintext.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static final Key<PsiNamedElement> DELEGATE = new Key<>("OMT_YAML_DELEGATE");

    private OMTYamlDelegateFactory() {
    }

    public static @Nullable PsiNamedElement createDelegate(YAMLPsiElement psiElement) {
        final PsiNamedElement omtYamlDelegate = DELEGATE.get(psiElement);
        if (omtYamlDelegate != null) {
            return omtYamlDelegate;
        }

        // create delegate by meta-type information:
        PsiNamedElement delegate = null;
        if (psiElement instanceof YAMLKeyValue) {
            delegate = createKeyValueDelegate((YAMLKeyValue) psiElement);
        } else if (psiElement instanceof YAMLPlainTextImpl) {
            delegate = createPlainTextDelegate((YAMLPlainTextImpl) psiElement);
        }
        if (delegate == null) {
            delegate = psiElement != null ? new NotADelegate(psiElement.getNode()) : null;
        }

        DELEGATE.set(psiElement, delegate);
        return delegate;
    }

    private static PsiNamedElement createKeyValueDelegate(YAMLKeyValue keyValue) {
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

    private static PsiNamedElement createPlainTextDelegate(YAMLPlainTextImpl yamlPlainText) {
        final OMTMetaTypeProvider instance = OMTMetaTypeProvider.getInstance(yamlPlainText.getProject());
        final YamlMetaType metaType = instance.getResolvedMetaType(yamlPlainText);
        if (metaType instanceof OMTParamMetaType) {
            return new OMTYamlParameterDelegate(yamlPlainText);
        } else if (metaType instanceof OMTBindingParameterMetaType) {
            return new OMTYamlBindingParameterDelegate(yamlPlainText);
        } else if (metaType instanceof OMTBaseParameterMetaType) {
            return new OMTYamlBaseParameterDelegate(yamlPlainText);
        } else if (metaType instanceof OMTNamedVariableMetaType) {
            return new OMTYamlVariableDelegate(yamlPlainText);
        } else if (metaType instanceof OMTImportMemberMetaType) {
            return new OMTYamlImportMemberDelegate(yamlPlainText);
        } else if (metaType instanceof OMTImportedMemberRefMetaType) {
            return new OMTYamlImportedMemberRefDelegate(yamlPlainText);
        } else if (metaType instanceof OMTPayloadQueryReferenceMetaType) {
            return new OMTYamlPayloadQueryReferenceDelegate(yamlPlainText);
        } else if (metaType instanceof OMTOntologyPrefixMetaType) {
            return new OMTYamlOntologyPrefixDelegate(yamlPlainText);
        } else if (metaType instanceof OMTParamTypeMetaType) {
            return new OMTYamlParamTypeDelegate(yamlPlainText);
        } else if (metaType instanceof OMTFileReferenceMetaType) {
            return new OMTYamlFileReferenceDelegate(yamlPlainText);
        } else if (metaType instanceof OMTGraphShapeHandlerMemberMetaType) {
            return new OMTYamlGraphShapeHandlerMemberDelegate(yamlPlainText);
        }
        return null;
    }

    private static class NotADelegate extends ASTWrapperPsiElement implements PsiNamedElement {

        public NotADelegate(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
            return null;
        }
    }
}
