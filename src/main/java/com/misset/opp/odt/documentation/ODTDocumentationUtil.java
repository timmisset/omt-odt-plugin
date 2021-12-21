package com.misset.opp.odt.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.prefix.PrefixUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ODTDocumentationUtil {
    private static final List<Class<? extends PsiElement>> validDocOwners = List.of(
            ODTDefineStatement.class
    );

    @Nullable
    public static String getJavaDocCommentDescription(@NotNull PsiElement element) {
        PsiDocComment psiDocComment = getJavaDocComment(element);
        if (psiDocComment != null) {
            return Arrays.stream(psiDocComment.getDescriptionElements())
                    .map(PsiElement::getText)
                    .collect(Collectors.joining("<br>"));
        }
        return null;
    }

    @Nullable
    public static PsiDocComment getJavaDocComment(@NotNull PsiElement element) {
        if (element.getParent() instanceof PsiJavaDocumentedElement) {
            return ((PsiJavaDocumentedElement) element.getParent()).getDocComment();
        }
        return null;
    }

    @Nullable
    public static PsiElement getDocOwner(PsiDocTag docTag) {
        PsiElement psiElement = PsiTreeUtil.nextVisibleLeaf(docTag.getContainingComment());
        while (psiElement != null && !isValidDocOwner(psiElement)) {
            psiElement = psiElement.getParent();
        }
        return psiElement;
    }

    private static boolean isValidDocOwner(PsiElement element) {
        return element != null && validDocOwners.stream()
                .anyMatch(docOwnerClass -> docOwnerClass.isAssignableFrom(element.getClass()));
    }

    private static final Pattern LOCAL_NAME = Pattern.compile("\\([^:]*:([^)]*)\\)");

    public static Set<OntResource> getTypeFromDocTag(PsiDocTag docTag, int dataElementPosition) {
        if (docTag != null && docTag.getDataElements().length > dataElementPosition) {
            // we can retrieve the type from the DocTag:
            final PsiElement dataElement = docTag.getDataElements()[dataElementPosition];
            final PsiReference[] references = docTag.getReferences();
            final Optional<PsiReference> curieReference = Arrays.stream(references)
                    .filter(psiReference -> psiReference.getRangeInElement()
                            .intersects(dataElement.getTextRangeInParent()))
                    .findFirst();
            if (curieReference.isPresent()) {
                // there is a reference available for the type, meaning we should try to resolve it to the prefix
                // and generate a fully qualified URI from it:
                final PsiElement prefix = curieReference.get().resolve();
                if (prefix instanceof YAMLKeyValue) {
                    final Matcher matcher = LOCAL_NAME.matcher(dataElement.getText());
                    if (matcher.find()) {
                        return Optional.ofNullable(PrefixUtil.getFullyQualifiedUri((YAMLKeyValue) prefix,
                                        matcher.group(1)))
                                .map(OppModel.INSTANCE::getClassIndividuals)
                                .stream()
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet());
                    }
                }
            } else {
                // no curie reference, probably a primitive type:
                // (string)
                final String value = dataElement.getText().replaceAll("[()]", "");
                return Optional.ofNullable(TTLValueParserUtil.parsePrimitive(value))
                        .map(OntResource.class::cast)
                        .map(Set::of)
                        .orElse(Collections.emptySet());
            }
        }
        return Collections.emptySet();
    }
}
