package com.misset.opp.omt;

import com.intellij.codeInsight.folding.JavaCodeFoldingSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTFoldingBuilder;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.folding.YAMLFoldingBuilder;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class OMTFoldingBuilder extends YAMLFoldingBuilder {
    private static final ODTFoldingBuilder odtFoldingBuilder = new ODTFoldingBuilder();

    @Override
    protected void buildLanguageFoldRegions(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiElement root, @NotNull Document document, boolean quick) {
        // get the root foldings
        super.buildLanguageFoldRegions(descriptors, root, document, quick);

        // append with foldings from inject language
        InjectedLanguageManager instance = InjectedLanguageManager.getInstance(root.getProject());

        Collection<InjectionHost> injectionHosts = PsiTreeUtil.findChildrenOfType(root, InjectionHost.class);

        // workaround for:
        // https://youtrack.jetbrains.com/issue/IDEA-289722
        for (InjectionHost injectionHost : injectionHosts) {
            List<Pair<PsiElement, TextRange>> injectedPsiFiles = instance.getInjectedPsiFiles(injectionHost);
            if (injectedPsiFiles != null) {
                injectedPsiFiles.forEach(
                        pair -> addInjectedFoldingDescriptors(odtFoldingBuilder, pair, descriptors, injectionHost.getTextOffset())
                );
            }
        }
    }

    private void addInjectedFoldingDescriptors(ODTFoldingBuilder odtFoldingBuilder,
                                               Pair<PsiElement, TextRange> psiElementTextRangePair,
                                               @NotNull List<FoldingDescriptor> descriptors,
                                               int hostOffset) {
        PsiElement element = psiElementTextRangePair.first;
        int startOffset = psiElementTextRangePair.second.getStartOffset();
        startOffset += hostOffset;


        descriptors.addAll(Arrays.asList(odtFoldingBuilder
                .buildFoldRegionsWithOffset(element, startOffset)));
    }

    @Override
    protected boolean isRegionCollapsedByDefault(@NotNull FoldingDescriptor descriptor) {
        return isCollapsedByDefault(
                descriptor.getElement().getPsi(),
                () -> super.isRegionCollapsedByDefault(descriptor)
        );
    }

    @Override
    protected boolean isRegionCollapsedByDefault(@NotNull ASTNode node) {
        return isCollapsedByDefault(node.getPsi(), () -> super.isRegionCollapsedByDefault(node));
    }

    private boolean isCollapsedByDefault(PsiElement element, Supplier<Boolean> ifNotApplicable) {
        // workaround for:
        // https://youtrack.jetbrains.com/issue/IDEA-289722
        if (element.getLanguage() == ODTLanguage.INSTANCE) {
            return odtFoldingBuilder.isCollapsedByDefault(element.getNode());
        }
        if (!(element instanceof YAMLKeyValue)) {
            return ifNotApplicable.get();
        }

        final JavaCodeFoldingSettings settings = JavaCodeFoldingSettings.getInstance();
        boolean collapseImports = settings.isCollapseImports();
        if (!collapseImports) {
            return false;
        }

        return Optional.of(OMTMetaTypeProvider.getInstance(element.getProject()))
                .map(metaTypeProvider -> metaTypeProvider.getKeyValueMetaType((YAMLKeyValue) element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .map(OMTImportMetaType.class::isInstance)
                .orElse(ifNotApplicable.get());
    }
}
