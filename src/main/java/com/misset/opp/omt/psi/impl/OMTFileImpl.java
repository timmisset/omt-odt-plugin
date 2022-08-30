package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.VirtualDirectoryImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.meta.OMTFileMetaType;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.*;
import java.util.stream.Collectors;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    private static final Key<CachedValue<Map<String, Collection<PsiCallable>>>> EXPORTING_MEMBERS = new Key<>("EXPORTING_MEMBERS");
    private static final Key<CachedValue<Map<String, Collection<PsiCallable>>>> EXPORTING_DECLARED_MEMBERS = new Key<>(
            "EXPORTING_DECLARED_MEMBERS");
    private static final Key<CachedValue<Map<String, Collection<PsiCallable>>>> IMPORTING_MEMBERS = new Key<>(
            "IMPORTING_MEMBERS");

    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    @Override
    public YAMLMapping getRootMapping() {
        return ReadAction.compute(() -> Optional.ofNullable(PsiTreeUtil.findChildOfType(this, YAMLDocument.class))
                .map(yamlDocument -> PsiTreeUtil.findChildOfType(yamlDocument, YAMLMapping.class))
                .orElse(null));
    }

    @Override
    @NotNull
    public SearchScope getUseScope() {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.projectScope(getProject()), OMTFileType.INSTANCE);
    }

    /**
     * Returns all the exportable members of this file, including members that are imported from other files
     */
    @Override
    public Map<String, Collection<PsiCallable>> getExportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_MEMBERS, () -> {
            final HashMap<String, Collection<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getExportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private HashMap<String, Collection<PsiCallable>> getExportingMembersMap(YAMLMapping yamlMapping) {
        return ReadAction.compute(() -> OMTFileMetaType.getInstance().getCallableMap(yamlMapping));
    }

    /**
     * Returns the exportable members that are declared in this file
     */
    @Override
    public Map<String, Collection<PsiCallable>> getDeclaredExportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_DECLARED_MEMBERS, () -> {
            final Map<String, Collection<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getDeclaredExportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private Map<String, Collection<PsiCallable>> getDeclaredExportingMembersMap(YAMLMapping yamlMapping) {
        return ReadAction.compute(() -> OMTFileMetaType.getInstance().getDeclaredCallableMap(yamlMapping, null));
    }

    /**
     * Returns the exportable members that are declared in this file
     */
    @Override
    public Map<String, Collection<PsiCallable>> getImportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, IMPORTING_MEMBERS, () -> {
            final Map<String, Collection<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getImportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private Map<String, Collection<PsiCallable>> getImportingMembersMap(YAMLMapping yamlMapping) {
        return ReadAction.compute(() -> OMTFileMetaType.getInstance().getImportingMembers(yamlMapping));
    }

    @Override
    public Map<String, String> getAvailableNamespaces(PsiElement element) {
        return OMTMetaTreeUtil.collectMetaParents(element, YAMLMapping.class, OMTPrefixProvider.class)
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getNamespaces(entry.getKey()))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, s2) -> s));
    }

    @Override
    @SuppressWarnings("java:S2637")
    public String getModuleName() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(getModuleFile(), YAMLMapping.class))
                .map(mapping -> mapping.getKeyValueByKey("moduleName"))
                .map(YAMLKeyValue::getValueText)
                .orElse(null);
    }

    @Override
    public OMTFile getModuleFile() {
        if (OMTFile.isModuleFileName(getName())) {
            return this;
        }

        VirtualFile directory = getVirtualFile().getParent();
        PsiManager psiManager = PsiManager.getInstance(getProject());
        while (directory instanceof VirtualDirectoryImpl) {
            Optional<VirtualFile> moduleFile = Arrays.stream(directory.getChildren())
                    .filter(virtualFile -> virtualFile.getName().contains(".module.omt"))
                    .findFirst();
            if (moduleFile.isPresent()) {
                return (OMTFile) psiManager.findFile(moduleFile.get());
            }
            directory = directory.getParent();
        }
        return null;
    }

}
