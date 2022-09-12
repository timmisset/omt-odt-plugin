package com.misset.opp.omt;

import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.psi.impl.yaml.OMTOverride;
import org.jetbrains.annotations.NotNull;

/**
 * All implementations of ContributedReferenceHost require their references to be provided via this route
 * The @OMTOverride classes should orchestrate their references themselves though, which is why this is
 * simply a proxy that will defer to the element.getReferences()
 */
public class OMTYamlReferenceContributor extends PsiReferenceContributor {
    private PatternCondition<PsiElement> patternCondition = new PatternCondition<>("OMTYAMLOverride") {
        @Override
        public boolean accepts(@NotNull PsiElement element,
                               ProcessingContext context) {
            return element.getClass().isAnnotationPresent(OMTOverride.class);
        }
    };

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement().with(patternCondition),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        return element.getReferences();
                    }
                });
    }
}
