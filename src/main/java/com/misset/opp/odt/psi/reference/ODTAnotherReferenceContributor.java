package com.misset.opp.odt.psi.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ODTAnotherReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(), new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                if(0 ==0 ) { throw new RuntimeException("Oops!"); }
                if (!(element instanceof PsiDocComment)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                final PsiDocComment docComment = (PsiDocComment) element;
                return Arrays.stream(docComment.findTagsByName("param"))
                        .filter(psiDocTag -> psiDocTag.getValueElement() != null)
                        .map(psiDocTag -> new ODTParameterAnnotationReference(psiDocTag, psiDocTag.getValueElement().getTextRangeInParent()))
                        .collect(Collectors.toList())
                        .toArray(PsiReference[]::new);
            }
        });
    }

}
