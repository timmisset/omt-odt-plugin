package com.misset.opp.omt;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTKeyValueImpl;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTPlainTextImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFindUsagesProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTFindUsageProvider extends YAMLFindUsagesProvider implements FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        if (psiElement instanceof YAMLOMTKeyValueImpl || psiElement instanceof YAMLOMTPlainTextImpl) {
            final YamlMetaType metaType = getMetaValueType(psiElement);
            return metaType instanceof OMTModelItemMetaType ||
                    metaType instanceof OMTNamedVariableMetaType ||
                    metaType instanceof OMTIriMetaType;
        }
        return super.canFindUsagesFor(psiElement);
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        if (element instanceof YAMLOMTKeyValueImpl || element instanceof YAMLOMTPlainTextImpl) {
            final YamlMetaType metaType = getMetaValueType(element);
            if (metaType instanceof OMTModelItemMetaType) {
                return metaType.getTypeName();
            } else if (metaType instanceof OMTIriMetaType) {
                return "prefix";
            } else if (metaType instanceof OMTNamedVariableMetaType) {
                return "variable";
            }
        }
        return super.getType(element);
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof YAMLOMTKeyValueImpl) {
            final YamlMetaType metaType = getMetaValueType(element);
            if (metaType instanceof OMTModelItemMetaType) {
                return metaType.getTypeName();
            }
            return ((YAMLKeyValue) element).getKeyText();
        } else if (element instanceof YAMLOMTPlainTextImpl) {
            final YamlMetaType metaType = getMetaType(element);
            if (metaType instanceof OMTNamedVariableMetaType) {
                return ((OMTNamedVariableMetaType) metaType).getName((YAMLValue) element);
            }
        }
        return super.getDescriptiveName(element);
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element,
                                            boolean useFullName) {
        if (element instanceof YAMLOMTKeyValueImpl || element instanceof YAMLOMTPlainTextImpl) {
            return getDescriptiveName(element);
        }
        return super.getNodeText(element, useFullName);
    }

    private YamlMetaType getMetaType(PsiElement element) {
        return OMTMetaTypeProvider.getInstance(element.getProject()).getResolvedMetaType(element);
    }

    private YamlMetaType getMetaValueType(PsiElement element) {
        if (element instanceof YAMLKeyValue) {
            return OMTMetaTypeProvider.getInstance(element.getProject())
                    .getResolvedKeyValueMetaTypeMeta((YAMLKeyValue) element);
        }
        return getMetaType(element);
    }
}
