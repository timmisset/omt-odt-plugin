package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLPsiElement;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    private static final Key<CachedValue<YAMLPsiElement>> HOST = new Key<>("HOST");
    public ODTFileImpl(@NotNull FileViewProvider provider) {
        super(provider, ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ODTFileType.INSTANCE;
    }

    public @Nullable YAMLPsiElement getHost() {
        return CachedValuesManager.getCachedValue(this, HOST, () -> {
            final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(getProject());
            final PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(this);
            if (!(injectionHost instanceof YAMLPsiElement)) {
                return new CachedValueProvider.Result<>(null, ModificationTracker.NEVER_CHANGED);
            }
            return new CachedValueProvider.Result<>((YAMLPsiElement) injectionHost, ModificationTracker.NEVER_CHANGED);
        });
    }
}
