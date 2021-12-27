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
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.*;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.impl.prefix.ODTBaseDefinePrefix;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.providers.OMTMetaTypeStructureProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    private static final Key<CachedValue<InjectionHost>> HOST = new Key<>("HOST");
    private static final Key<CachedValue<OMTFile>> HOST_FILE = new Key<>("HOST_FILE");
    private static final Key<CachedValue<Boolean>> IS_EXPORTABLE = new Key<>("IS_EXPORTABLE");
    private static final Key<CachedValue<Map<String, String>>> NAMESPACES = new Key<>("NAMESPACES");
    private static final Key<CachedValue<List<ODTNamespacePrefix>>> PREFIXES = new Key<>("PREFIXES");

    public ODTFileImpl(@NotNull FileViewProvider provider) {
        super(provider, ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ODTFileType.INSTANCE;
    }

    public InjectionHost getHost() {
        return CachedValuesManager.getCachedValue(this, HOST, () -> {
            final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(getProject());
            final PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(this);
            if (!(injectionHost instanceof InjectionHost)) {
                return new CachedValueProvider.Result<>(null,
                        this,
                        PsiModificationTracker.MODIFICATION_COUNT);
            }
            return new CachedValueProvider.Result<>((InjectionHost) injectionHost,
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
                    omtFile == null ? this : omtFile,
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
            psiFiles.addAll(OMTImportedMembersIndex.getImportingFiles(name));
        }
        final List<VirtualFile> targetFiles = psiFiles.stream()
                .map(PsiFile::getVirtualFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        if (isExportable()) {
            // also, all module files can declare the member as reference
            targetFiles.addAll(FilenameIndex.getAllFilesByExt(getProject(), "module.omt"));
        }
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

    @Override
    public <T extends OMTMetaTypeStructureProvider, U extends PsiElement> Optional<List<U>> resolveInOMT(Class<T> providerClass,
                                                                                                         Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> metaTypeStructureKey,
                                                                                                         String key,
                                                                                                         BiFunction<T, YAMLMapping, HashMap<String, List<U>>> mapFunction) {
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
                namespaces = hostFile.getAvailableNamespaces(getHost());
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

    @Override
    public List<ODTNamespacePrefix> getLocalNamespacePrefixes() {
        return CachedValuesManager.getCachedValue(this, PREFIXES, () -> {
            List<ODTNamespacePrefix> prefixes = PsiTreeUtil.findChildrenOfType(this, ODTDefinePrefix.class)
                    .stream()
                    .map(ODTDefinePrefix::getNamespacePrefix)
                    .collect(Collectors.toList());
            return new CachedValueProvider.Result<>(prefixes, this);
        });
    }

    private HashMap<String, List<PsiElement>> hostPrefixNamespaces = new HashMap<>();

    @Override
    public List<PsiElement> getHostPrefixNamespace(String key) {
        if (getHost() == null) {
            return Collections.emptyList();
        }
        if (hostPrefixNamespaces.containsKey(key)) {
            return hostPrefixNamespaces.get(key);
        }

        List<PsiElement> psiElements = resolveInOMT(OMTPrefixProvider.class,
                OMTPrefixProvider.KEY,
                key,
                OMTPrefixProvider::getPrefixMap)
                .orElse(Collections.emptyList());
        hostPrefixNamespaces.put(key, psiElements);
        return psiElements;
    }
}
