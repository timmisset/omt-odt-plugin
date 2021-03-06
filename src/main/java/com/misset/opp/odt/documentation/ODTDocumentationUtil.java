package com.misset.opp.odt.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.prefix.PrefixUtil;
import com.misset.opp.odt.refactoring.ODTRefactoringUtil;
import com.misset.opp.ttl.model.OppModel;
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
        return Optional.ofNullable(psiDocComment)
                .map(ODTDocumentationUtil::getCommentFromDescriptionElements)
                .orElse(null);

    }

    private static String getCommentFromDescriptionElements(PsiDocComment psiDocComment) {
        return Arrays.stream(psiDocComment.getDescriptionElements())
                .map(PsiElement::getText)
                .collect(Collectors.joining("<br>"));
    }

    @Nullable
    public static PsiDocComment getJavaDocComment(@NotNull PsiElement element) {
        return Optional.of(element.getParent())
                .filter(PsiJavaDocumentedElement.class::isInstance)
                .map(PsiJavaDocumentedElement.class::cast)
                .map(PsiJavaDocumentedElement::getDocComment)
                .orElse(null);
    }

    @Nullable
    public static PsiElement getDocOwner(PsiDocTag docTag) {
        PsiElement psiElement = PsiTreeUtil.nextVisibleLeaf(docTag.getContainingComment());
        while (psiElement != null && !isValidDocOwner(psiElement)) {
            psiElement = psiElement.getParent();
        }
        return psiElement;
    }

    public static void removeDocTag(PsiDocTag docTag) {
        PsiDocComment containingComment = docTag.getContainingComment();
        docTag.delete();
        if (isEmptyComment(containingComment)) {
            removeComment(containingComment);
        }
    }

    private static void removeComment(PsiDocComment comment) {
        ODTRefactoringUtil.removeWhitespace(comment);
        comment.delete();
    }

    private static boolean isEmptyComment(PsiDocComment comment) {
        PsiDocTag[] tags = comment.getTags();
        if (tags.length > 0) {
            return false;
        }

        return Optional.of(comment.getDescriptionElements())
                .filter(elements -> elements.length > 0)
                .map(ODTDocumentationUtil::allDescriptionsEmpty)
                .orElse(true);
    }

    private static boolean allDescriptionsEmpty(PsiElement[] descriptionElements) {
        return Arrays.stream(descriptionElements)
                .map(PsiElement::getText)
                .allMatch(String::isBlank);
    }

    private static boolean isValidDocOwner(PsiElement element) {
        return element != null && validDocOwners.stream()
                .anyMatch(docOwnerClass -> docOwnerClass.isAssignableFrom(element.getClass()));
    }

    private static final Pattern LOCAL_NAME = Pattern.compile("\\([^:]*:([^)]*)\\)");

    public static Set<OntResource> getTypeFromDocTag(@Nullable PsiDocTag docTag, int dataElementPosition) {
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
                final Matcher matcher = LOCAL_NAME.matcher(dataElement.getText());
                if (matcher.find()) {
                    String localName = matcher.group(1);
                    String uri = null;
                    if (prefix instanceof YAMLKeyValue) {
                        uri = PrefixUtil.getFullyQualifiedUri((YAMLKeyValue) prefix, localName);
                    } else if (prefix != null && prefix.getParent() instanceof ODTDefinePrefix) {
                        uri = PrefixUtil.getFullyQualifiedUri((ODTDefinePrefix) prefix.getParent(), localName);
                    }
                    return Optional.ofNullable(uri)
                            .map(OppModel.INSTANCE::toIndividuals)
                            .stream()
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet());
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
