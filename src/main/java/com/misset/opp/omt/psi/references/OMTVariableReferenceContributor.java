package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Generate a reference for $variable elements that are part of the OMT language
 */
public class OMTVariableReferenceContributor extends PsiReferenceContributor {
    private static final Pattern PATTERN = Pattern.compile("\\$[^\\s]*");
    private static final @NotNull PsiElementPattern.Capture<YAMLPlainTextImpl> VARIABLE =
            psiElement(YAMLPlainTextImpl.class)
                    .withText("$");

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(VARIABLE, new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                if(element instanceof YAMLPlainTextImpl) {
                    return getVariableReferences((YAMLPlainTextImpl) element);
                }
                return new PsiReference[0];
            }
        });
    }

    private PsiReference[] getVariableReferences(YAMLPlainTextImpl text) {
        final Matcher matcher = PATTERN.matcher(text.getTextValue());
        List<PsiReference> referenceList = new ArrayList<>();
        while(matcher.find()) {
            referenceList.add(
                    new OMTVariableReference(text, TextRange.create(matcher.start(), matcher.end()))
            );
        }
        return referenceList.toArray(PsiReference[]::new);
    }
}
