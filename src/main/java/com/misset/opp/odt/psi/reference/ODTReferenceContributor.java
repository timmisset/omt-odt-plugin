package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTIgnored;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ODTReferenceContributor extends PsiReferenceContributor {
    private static final Pattern PARAM_ANNOTATION = Pattern.compile(
            "@param\\s(\\$[A-z]*)\\s(\\(([A-z0-9]*)(:)?([A-z0-9]*)\\))?");

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(PsiComment.class), new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                if (!(element instanceof PsiComment)) {
                    return PsiReference.EMPTY_ARRAY;
                }

                if (((PsiCommentImpl) element).getTokenType() == ODTIgnored.JAVADOCS) {
                    return getJavaDocsReferences((PsiComment) element);
                }
                return new PsiReference[0];
            }
        });
    }

    private PsiReference[] getJavaDocsReferences(PsiComment comment) {
        List<PsiReference> collection = new ArrayList<>();
        addJavaDocsParamReferences(collection, comment);

        return collection.toArray(PsiReference[]::new);
    }

    private void addJavaDocsParamReferences(List<PsiReference> collection,
                                            PsiComment comment) {
        final Matcher matcher = PARAM_ANNOTATION.matcher(comment.getText());
        while (matcher.find()) {
            // return a reference for the variable:
            collection.add(
                    new ODTParameterAnnotationReference(
                            comment,
                            TextRange.create(matcher.start(1), matcher.end(1)),
                            matcher.group(2)));
            // and a reference for the parameter type if using a curie
            if (matcher.group(4) != null) {
                collection.add(
                        new ODTTypeAnnotationReference(
                                comment,
                                TextRange.create(matcher.start(3), matcher.end(3))));
            }
        }
    }
}
