package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFile;
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
import com.misset.opp.omt.indexing.ImportedMembersIndex;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTMetaTypeStructureProvider;
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
import java.util.Objects;
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
                        this,
                        PsiModificationTracker.MODIFICATION_COUNT);
            }
            return new CachedValueProvider.Result<>((YAMLPsiElement) injectionHost,
                    injectionHost.getContainingFile(),
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
                    omtFile,
                    PsiModificationTracker.MODIFICATION_COUNT);
        });
    }

    public PsiFile getHostOrContaining() {
        return Optional.ofNullable(getHostFile())
                .map(PsiFile.class::cast)
                .orElse(getContainingFile());
    }

    public SearchScope getExportingMemberUseScope(String name) {
        final ArrayList<PsiFile> psiFiles = new ArrayList<>();
        psiFiles.add(getHostOrContaining());
        if (isExportable()) {
            psiFiles.addAll(ImportedMembersIndex.getImportingFiles(name));
        }
        final List<VirtualFile> targetFiles = psiFiles.stream()
                .map(PsiFile::getVirtualFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return GlobalSearchScope.filesScope(getProject(), targetFiles);
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
            return getCachedValue(isExportable);
        });
    }

    public <T extends OMTMetaTypeStructureProvider> YAMLMapping getClosestProvider(Class<T> metaTypeOrInterface,
                                                                                   Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> key) {
        final LinkedHashMap<YAMLMapping, T> providers = getProviders(metaTypeOrInterface, key);
        return providers.keySet().stream().findFirst().orElse(null);
    }

    public <T extends OMTMetaTypeStructureProvider> LinkedHashMap<YAMLMapping, T> getProviders(Class<T> metaTypeOrInterface,
                                                                                               Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> key) {
        return getProviders(YAMLMapping.class, metaTypeOrInterface, key);
    }

    public <T extends YAMLPsiElement, U extends OMTMetaTypeStructureProvider> LinkedHashMap<T, U> getProviders(Class<T> yamlType,
                                                                                                               Class<U> metaTypeOrInterface,
                                                                                                               Key<CachedValue<LinkedHashMap<T, U>>> key) {
        /*
            It's important to set the modification tracker to MODIFICATION_COUNT
            This will make sure there is no PSI information retained which could cause memory leaks
         */
        return CachedValuesManager.getCachedValue(this,
                key,
                () -> getCachedValue(ODTInjectionUtil.getProviders(
                        this,
                        yamlType,
                        metaTypeOrInterface)));
    }

    public <T extends OMTMetaTypeStructureProvider> Optional<ResolveResult[]> resolveInOMT(Class<T> providerClass,
                                                                                           Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> metaTypeStructureKey,
                                                                                           String key,
                                                                                           BiFunction<T, YAMLMapping, HashMap<String, List<PsiElement>>> mapFunction) {
        final LinkedHashMap<YAMLMapping, T> providers = getProviders(providerClass, metaTypeStructureKey);
        return OMTMetaTreeUtil.resolveProvider(providers, key, mapFunction);
    }

    @Override
    public Map<String, String> getAvailableNamespaces() {
        return CachedValuesManager.getCachedValue(this, NAMESPACES, () -> {
            final OMTFile hostFile = getHostFile();
            final Map<String, String> namespaces;
            if (hostFile != null) {
                // in OMT files
                namespaces = getProviders(OMTPrefixProvider.class, OMTPrefixProvider.KEY).entrySet().stream()
                        .map(entry -> entry.getValue().getNamespaces(entry.getKey()))
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (s, s2) -> s));// preserve the closest to the element, basically shadowing the parent declaration if applicable

            } else {
                // in ODT files
                namespaces =
                        PsiTreeUtil.findChildrenOfType(this, ODTBaseDefinePrefix.class)
                                .stream()
                                .collect(Collectors.toMap(ODTBaseDefinePrefix::getNamespace,
                                        ODTBaseDefinePrefix::getPrefix, (o, o2) -> o));
            }
            return getCachedValue(namespaces);
        });
    }

    @Override
    public <T> CachedValueProvider.Result<T> getCachedValue(T result,
                                                            ModificationTracker... additionalTrackers) {
        final OMTFile hostFile = getHostFile();
        return hostFile != null ? new CachedValueProvider.Result<>(result, hostFile, this, additionalTrackers) :
                new CachedValueProvider.Result<>(result, this, additionalTrackers);
    }

}
