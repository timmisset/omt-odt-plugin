package com.misset.opp.omt.symbols;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLFindUsagesProvider;

public class OMTFindUsagesProvider extends YAMLFindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof OMTVariable;
    }

}
