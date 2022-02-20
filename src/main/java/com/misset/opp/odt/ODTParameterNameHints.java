package com.misset.opp.odt;

import com.intellij.codeInsight.hints.InlayInfo;
import com.intellij.codeInsight.hints.InlayParameterHintsProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ODTParameterNameHints implements InlayParameterHintsProvider {
    @Override
    public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element) {
        if (!(element instanceof ODTSignatureArgument)) {
            return Collections.emptyList();
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
