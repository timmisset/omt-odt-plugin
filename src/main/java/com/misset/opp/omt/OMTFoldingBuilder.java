package com.misset.opp.omt;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTFoldingBuilder;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.folding.YAMLFoldingBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OMTFoldingBuilder extends YAMLFoldingBuilder {

    @Override
    protected void buildLanguageFoldRegions(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiElement root, @NotNull Document document, boolean quick) {
        // get the root foldings
        super.buildLanguageFoldRegions(descriptors, root, document, quick);

        // append with foldings from inject language
        InjectedLanguageManager instance = InjectedLanguageManager.getInstance(root.getProject());

        Collection<InjectionHost> injectionHosts = PsiTreeUtil.findChildrenOfType(root, InjectionHost.class);
        ODTFoldingBuilder odtFoldingBuilder = new ODTFoldingBuilder();

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
}
