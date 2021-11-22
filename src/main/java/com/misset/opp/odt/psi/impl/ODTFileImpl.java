package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.prefix.ODTBaseDefinePrefix;
import com.misset.opp.omt.OMTLanguage;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    private static final Key<CachedValue<YAMLPsiElement>> HOST = new Key<>("HOST");
    private static final Key<CachedValue<OMTFile>> HOST_FILE = new Key<>("HOST_FILE");
    private static final Key<CachedValue<GlobalSearchScope>> EXPORTING_MEMBER_SCOPE = new Key<>("EXPORTING_MEMBER_SCOPE");
    private static final Key<CachedValue<Boolean>> IS_EXPORTABLE = new Key<>("IS_EXPORTABLE");
    private static final Key<CachedValue<Map<String, String>>> NAMESPACES = new Key<>("NAMESPACES");

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
                return new CachedValueProvider.Result<>(null,
                        getContainingFile(),
                        PsiModificationTracker.MODIFICATION_COUNT);
            }
            return new CachedValueProvider.Result<>((YAMLPsiElement) injectionHost,
                    getContainingFile(),
                    PsiModificationTracker.MODIFICATION_COUNT);
        });
    }

    public @Nullable OMTFile getHostFile() {
        return CachedValuesManager.getCachedValue(this, HOST_FILE, () -> {
            final OMTFile omtFile = Optional.ofNullable(getHost())
                    .map(PsiElement::getContainingFile)
                    .filter(OMTFile.class::isInstance)
                    .map(OMTFile.class::cast)
                    .orElse(null);
            return new CachedValueProvider.Result<>(omtFile,
                    getContainingFile(),
                    PsiModificationTracker.MODIFICATION_COUNT);
        });
    }

    /**
     * Returns
     */
    public SearchScope getExportingMemberUseScope() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_MEMBER_SCOPE, () ->
        {
            final OMTFile hostFile = getHostFile();
            final GlobalSearchScope scope;
            if (hostFile == null) {
                scope = GlobalSearchScope.fileScope(this);
                return new CachedValueProvider.Result<>(scope, this);
            } else {
                List<OMTFile> files = new ArrayList<>();
                files.add(hostFile);
                if (isExportable()) {
                    files.addAll(hostFile.getImportedBy());
                }

                scope = GlobalSearchScope.filesScope(getProject(),
                        files.stream().map(PsiFile::getVirtualFile).collect(
                                Collectors.toSet()));

                return new CachedValueProvider.Result<>(scope,
                        OMTLanguage.getLanguageModificationTracker(getProject()));
            }
        });
    }

    @Override
    public boolean isExportable() {
        return CachedValuesManager.getCachedValue(this, IS_EXPORTABLE, () -> {
            final OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(getProject());
            final Boolean isExportable = Optional.ofNullable(getHost())
                    .filter(YAMLValue.class::isInstance)
                    .map(YAMLValue.class::cast)
                    .map(metaTypeProvider::getValueMetaType)
                    .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                    .filter(OMTScriptMetaType.class::isInstance)
                    .map(OMTScriptMetaType.class::cast)
                    .map(OMTScriptMetaType::isExportable)
                    .orElse(false);
            return new CachedValueProvider.Result<>(isExportable,
                    getContainingFile(),
                    PsiModificationTracker.MODIFICATION_COUNT);
        });
    }

    public <T> YAMLMapping getClosestProvider(Class<T> metaTypeOrInterface) {
        final LinkedHashMap<YAMLMapping, T> providers = getProviders(metaTypeOrInterface);
        return providers.keySet().stream().findFirst().orElse(null);
    }

    public <T> LinkedHashMap<YAMLMapping, T> getProviders(Class<T> metaTypeOrInterface) {
        return getProviders(YAMLMapping.class, metaTypeOrInterface);
    }

    public <T extends YAMLPsiElement, U> LinkedHashMap<T, U> getProviders(Class<T> yamlType,
                                                                          Class<U> metaTypeOrInterface) {
        /*
            It's important to set the modification tracker to MODIFICATION_COUNT
            This will make sure there is no PSI information retained which could cause memory leaks
         */
        Key<CachedValue<LinkedHashMap<T, U>>> metaKey = new Key<>("META_" + metaTypeOrInterface.getSimpleName());
        return CachedValuesManager.getCachedValue(this,
                metaKey,
                () -> new CachedValueProvider.Result<>(ODTInjectionUtil.getProviders(
                        this,
                        yamlType,
                        metaTypeOrInterface), getContainingFile(), PsiModificationTracker.MODIFICATION_COUNT));
    }

    public <T> Optional<ResolveResult[]> resolveInOMT(Class<T> providerClass,
                                                      String key,
                                                      BiFunction<T, YAMLMapping, HashMap<String, List<PsiElement>>> mapFunction) {
        final LinkedHashMap<YAMLMapping, T> providers = getProviders(providerClass);
        return OMTMetaTreeUtil.resolveProvider(providers, key, mapFunction);
    }

    @Override
    public Map<String, String> getAvailableNamespaces() {
        return CachedValuesManager.getCachedValue(this, NAMESPACES, () -> {
            final OMTFile hostFile = getHostFile();
            if (hostFile != null) {
                // in OMT files
                final Map<String, String> namespaces = getProviders(OMTPrefixProvider.class).entrySet().stream()
                        .map(entry -> entry.getValue().getNamespaces(entry.getKey()))
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (s, s2) -> s));// preserve the closest to the element, basically shadowing the parent declaration if applicable
                return new CachedValueProvider.Result<>(namespaces, hostFile);
            } else {
                // in ODT files
                final Map<String, String> namespaces =
                        PsiTreeUtil.findChildrenOfType(this, ODTBaseDefinePrefix.class)
                                .stream()
                                .collect(Collectors.toMap(ODTBaseDefinePrefix::getNamespace,
                                        ODTBaseDefinePrefix::getPrefix, (o, o2) -> o));
                return new CachedValueProvider.Result<>(namespaces, getContainingFile());
            }
        });
    }

}
