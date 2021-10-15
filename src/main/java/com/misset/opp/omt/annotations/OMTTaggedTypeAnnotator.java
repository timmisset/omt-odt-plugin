package com.misset.opp.omt.annotations;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTaggedType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;

/**
 * Certain elements in the OMT language require their values to be tagged, such as a modelitem mapping (!Activity, !Component etc)
 * or a MergeHandler (!MergePredicates, !MergeLists).
 * <p>
 * To make this more apparent, annotate their keys with a specific color and tooltip to suggest possible values
 */
public class OMTTaggedTypeAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof YAMLKeyValue) {
            final YamlMetaTypeProvider.MetaTypeProxy keyValueMetaType = new OMTMetaTypeProvider().getKeyValueMetaType((YAMLKeyValue) element);
            if (keyValueMetaType != null &&
                    keyValueMetaType.getMetaType() instanceof OMTMetaTaggedType) {
                final OMTMetaTaggedType taggedType = (OMTMetaTaggedType) keyValueMetaType.getMetaType();
                final List<String> availableTags = taggedType.getAvailableTags();
                final PsiElement key = ((YAMLKeyValue) element).getKey();

                AnnotationBuilder annotationBuilder = holder.newAnnotation(HighlightSeverity.INFORMATION,
                        "Available tags: " + String.join(", ", availableTags));
                if (key != null) {
                    annotationBuilder = annotationBuilder.range(key);
                }
                annotationBuilder.create();
            }
        }

    }
}
