package com.misset.opp.omt;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTOntologyPrefixMetaType;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.omt.psi.references.OMTImportPathReference;
import com.misset.opp.omt.psi.references.OMTOntologyPrefixReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class OMTReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(YAMLPlainTextImpl.class), getYAMLPlainTextReferenceProvider());
        registrar.registerReferenceProvider(psiElement(YAMLKeyValue.class), getYAMLKeyValueReferenceProvider());
    }

    private PsiReferenceProvider getYAMLPlainTextReferenceProvider() {
        return new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                final YamlMetaType metaType = getMetaType(element);
                if(metaType instanceof OMTImportMemberMetaType) {
                    return new PsiReference[] { new OMTImportMemberReference((YAMLPlainTextImpl) element) };
                } else if (metaType instanceof OMTOntologyPrefixMetaType) {
                    return new PsiReference[] { new OMTOntologyPrefixReference((YAMLPlainTextImpl) element) };
                }
                return new PsiReference[0];
            }
        };
    }

    private PsiReferenceProvider getYAMLKeyValueReferenceProvider() {
        return new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                final YAMLKeyValue keyValue = (YAMLKeyValue) element;
                if(keyValue.getKey() != null && getMetaType(element) instanceof OMTImportMetaType) {
                    return new PsiReference[] { new OMTImportPathReference(keyValue, keyValue.getKey().getTextRangeInParent()) };
                }
                return new PsiReference[0];
            }
        };
    }

    private YamlMetaType getMetaType(PsiElement element) {
        OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());
        final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = metaTypeProvider.getMetaTypeProxy(element);
        return metaTypeProxy != null ? metaTypeProxy.getMetaType() : null;
    }

}
