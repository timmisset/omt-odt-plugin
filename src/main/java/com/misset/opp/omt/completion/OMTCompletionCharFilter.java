package com.misset.opp.omt.completion;

import com.intellij.codeInsight.lookup.CharFilter;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.OMTLanguage;
import org.jetbrains.annotations.Nullable;

public class OMTCompletionCharFilter extends CharFilter {
    @Override
    public @Nullable Result acceptChar(char c, int prefixLength, Lookup lookup) {
        PsiFile psiFile = lookup.getPsiFile();
        if (psiFile != null &&
                psiFile.getLanguage() == OMTLanguage.INSTANCE
                && c == ':') {
            return Result.ADD_TO_PREFIX;
        }

        return Result.HIDE_LOOKUP;
    }
}
