package com.misset.opp.omt;

import com.intellij.codeInsight.hints.InlayInfo;
import com.intellij.codeInsight.hints.InlayParameterHintsProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OMTParameterNameHints implements InlayParameterHintsProvider {
    @Override
    public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element) {
        if (!(element instanceof YAMLSequenceItem)) {
            return Collections.emptyList();
        }
        YAMLSequenceItem sequenceItem = (YAMLSequenceItem) element;
        YamlMetaType resolvedMetaType = OMTMetaTypeProvider.getInstance(element.getProject())
                .getResolvedMetaType(element);
        if (sequenceItem.getValue() != null && resolvedMetaType instanceof OMTImportMemberMetaType) {
            String type = Optional.ofNullable(sequenceItem.getValue().getReference())
                    .filter(OMTImportMemberReference.class::isInstance)
                    .map(OMTImportMemberReference.class::cast)
                    .map(reference -> reference.resolve(false))
                    .filter(PsiCallable.class::isInstance)
                    .map(PsiCallable.class::cast)
                    .map(Callable::getType)
                    .orElse("unknown");
            return List.of(
                    new InlayInfo(
                            type, element.getTextOffset() + element.getTextLength(), false, false, true));
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull String getInlayPresentation(@NotNull String inlayText) {
        return inlayText;
    }

    @Override
    public @NotNull Set<String> getDefaultBlackList() {
        return Collections.emptySet();
    }

}
