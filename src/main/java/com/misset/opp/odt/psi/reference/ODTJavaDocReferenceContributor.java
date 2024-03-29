package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocTagImpl;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.util.UriPatternUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * The Javadoc comments that can be added into the ODT language as annotations are parsed as PsiDocComment.
 * This makes them effectively a JAVA language element which is why this contributor has to be registered to
 * the JAVA language, not the ODT
 * The PsiDocTags are the elements that use the 'getReferencesFromProviders' which triggers this contributor
 *
 * @see PsiDocTagImpl#getReferences()
 */
public class ODTJavaDocReferenceContributor extends PsiReferenceContributor {
    private static final Pattern ONTOLOGY = Pattern.compile("\\([^:]*:([^)]*)\\)");

    private static final Pattern QUALIFIED_URI = Pattern.compile("\\(<([^>]*)>\\)");

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(PsiDocTag.class), new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                   @NotNull ProcessingContext context) {
                final PsiDocTag docTag = (PsiDocTag) element;
                if (!(element.getContainingFile() instanceof ODTFile)) {
                    return PsiReference.EMPTY_ARRAY;
                }

                List<PsiReference> referenceList = new ArrayList<>();
                if (docTag.getName().equals("param") && docTag.getValueElement() != null) {
                    Optional.ofNullable(getParamReference(docTag)).ifPresent(referenceList::add);
                    Optional.ofNullable(getTypePrefixReference(docTag, 1)).ifPresent(referenceList::add);
                    Optional.ofNullable(getTypeOntologyReference(docTag, 1)).ifPresent(referenceList::add);
                } else if (List.of("base", "return").contains(docTag.getName()) && docTag.getValueElement() != null) {
                    Optional.ofNullable(getTypePrefixReference(docTag, 0)).ifPresent(referenceList::add);
                    Optional.ofNullable(getTypeOntologyReference(docTag, 0)).ifPresent(referenceList::add);
                }
                return referenceList.toArray(PsiReference[]::new);
            }
        });
    }

    private PsiReference getParamReference(PsiDocTag docTag) {
        // @param $param (ont:Class)
        // the valueElement == $param
        // create a reference which might be resolvable to the adjacent docOwner such as ODTDefineStatement that contains matching input parameters
        return Optional.ofNullable(docTag.getValueElement())
                .map(psiDocTagValue -> new ODTParameterAnnotationReference(docTag, psiDocTagValue.getTextRangeInParent()))
                .orElse(null);
    }

    private PsiReference getTypePrefixReference(PsiDocTag docTag, int position) {
        if (docTag.getDataElements().length > position) {
            final PsiElement dataElement = docTag.getDataElements()[position];
            String resourceReference = dataElement.getText();
            if (UriPatternUtil.isCurie(resourceReference)) {
                String prefix = UriPatternUtil.getPrefix(resourceReference);
                int offset = resourceReference.indexOf(prefix);
                return new ODTTypePrefixAnnotationReference(docTag,
                        TextRange.create(offset, offset + prefix.length())
                                .shiftRight(dataElement.getStartOffsetInParent()));
            }
        }
        return null;
    }

    private PsiReference getTypeOntologyReference(PsiDocTag docTag, int position) {
        if (docTag.getDataElements().length > position) {
            final PsiElement dataElement = docTag.getDataElements()[position];
            String resourceReference = dataElement.getText();
            if (UriPatternUtil.isUri(resourceReference) ||
                    UriPatternUtil.isCurie(resourceReference)) {
                TextRange localnameRange = UriPatternUtil.getLocalnameRange(resourceReference)
                        .shiftRight(dataElement.getStartOffsetInParent());
                return new ODTJavaDocTTLSubjectReference(docTag, localnameRange, position);
            }
        }
        return null;
    }
}
