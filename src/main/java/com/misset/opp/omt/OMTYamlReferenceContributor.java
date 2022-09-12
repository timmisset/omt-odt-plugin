package com.misset.opp.omt;

import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTKeyValueImpl;
import org.jetbrains.annotations.NotNull;

/**
 * The YamlKeyValueImpl is an implementation of the ContributedReferenceHost
 * This means that references for this element must be provided using this contributor.
 */
public class OMTYamlReferenceContributor extends PsiReferenceContributor {
    private PatternCondition<PsiElement> keyValues = new PatternCondition<>("YAMLOMTKeyValueImpl") {
        @Override
        public boolean accepts(@NotNull PsiElement element,
                               ProcessingContext context) {
            return element instanceof YAMLOMTKeyValueImpl;
        }
    };

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement().with(keyValues),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        return element.getReferences();
                    }
                });
    }
}
