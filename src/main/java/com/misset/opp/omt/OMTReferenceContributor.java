package com.misset.opp.omt;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class OMTReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(YAMLPlainTextImpl.class), getYAMLSequenceItemReferenceProvider(), 100);
    }

    private PsiReferenceProvider getYAMLSequenceItemReferenceProvider() {
        return new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());
                final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = metaTypeProvider.getMetaTypeProxy(element);
                if(metaTypeProxy != null && metaTypeProxy.getMetaType() instanceof OMTImportMemberMetaType) {
                    return new PsiReference[] { new OMTImportMemberReference((YAMLPlainTextImpl) element) };
                }
                return new PsiReference[0];
            }
        };
    }
}
