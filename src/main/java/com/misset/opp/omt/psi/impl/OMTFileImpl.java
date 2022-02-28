package com.misset.opp.omt.psi.impl;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.VirtualDirectoryImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.meta.OMTFileMetaType;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.shared.InjectionHost;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.*;
import java.util.stream.Collectors;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    private static final Key<CachedValue<HashMap<String, List<PsiCallable>>>> EXPORTING_MEMBERS = new Key<>("EXPORTING_MEMBERS");
    private static final Key<CachedValue<HashMap<String, List<PsiCallable>>>> EXPORTING_DECLARED_MEMBERS = new Key<>(
            "EXPORTING_DECLARED_MEMBERS");
    private static final Key<CachedValue<HashMap<String, List<PsiCallable>>>> IMPORTING_MEMBERS = new Key<>(
            "IMPORTING_MEMBERS");
    private static final Key<CachedValue<Map<String, List<PsiCall>>>> PSI_CALLS = new Key<>("PSI_CALLS");

    Logger LOGGER = Logger.getInstance(OMTFileImpl.class);

    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    @Override
    public YAMLMapping getRootMapping() {
        return LoggerUtil.computeWithLogger(LOGGER, "getRootMapping", () ->
                ReadAction.compute(() -> Optional.ofNullable(PsiTreeUtil.findChildOfType(this, YAMLDocument.class))
                        .map(yamlDocument -> PsiTreeUtil.findChildOfType(yamlDocument, YAMLMapping.class))
                        .orElse(null)));

    }

    @Override
    public Map<String, List<PsiCall>> getAllInjectedPsiCalls() {
        return CachedValuesManager.getCachedValue(
                this,
                PSI_CALLS,
                () -> new CachedValueProvider.Result<>(calculateAllInjectedPsiCalls(), this));
    }

    private Map<String, List<PsiCall>> calculateAllInjectedPsiCalls() {
        InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(getProject());
        return PsiTreeUtil.findChildrenOfType(this, InjectionHost.class)
                .stream()
                .map(languageManager::getInjectedPsiFiles)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(pair -> pair.getFirst())
                .map(this::getCallsFromInjectedFragment)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(PsiCall::getName));
    }

    private Collection<PsiCall> getCallsFromInjectedFragment(PsiElement injectedFragment) {
        return PsiTreeUtil.findChildrenOfType(injectedFragment, PsiCall.class);
    }

    /**
     * Returns all the exportable members of this file, including members that are imported from other files
     */
    @Override
    public HashMap<String, List<PsiCallable>> getExportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_MEMBERS, () -> {
            final HashMap<String, List<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getExportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private HashMap<String, List<PsiCallable>> getExportingMembersMap(YAMLMapping yamlMapping) {
        return LoggerUtil.computeWithLogger(LOGGER, "getExportingMembersMap", () ->
                ReadAction.compute(() -> new OMTFileMetaType("OMTFile").getCallableMap(yamlMapping)));
    }

    /**
     * Returns the exportable members that are declared in this file
     */
    @Override
    public HashMap<String, List<PsiCallable>> getDeclaredExportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_DECLARED_MEMBERS, () -> {
            final HashMap<String, List<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getDeclaredExportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private HashMap<String, List<PsiCallable>> getDeclaredExportingMembersMap(YAMLMapping yamlMapping) {
        return LoggerUtil.computeWithLogger(LOGGER, "getExportingMembersMap", () ->
                ReadAction.compute(() -> OMTFileMetaType.getDeclaredCallableMap(yamlMapping, null)));
    }

    /**
     * Returns the exportable members that are declared in this file
     */
    @Override
    public HashMap<String, List<PsiCallable>> getImportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, IMPORTING_MEMBERS, () -> {
            final HashMap<String, List<PsiCallable>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getImportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private HashMap<String, List<PsiCallable>> getImportingMembersMap(YAMLMapping yamlMapping) {
        return LoggerUtil.computeWithLogger(LOGGER, "getImportingMembersMap", () ->
                ReadAction.compute(() -> OMTFileMetaType.getImportingMembers(yamlMapping)));
    }

    @Override
    public Map<String, String> getAvailableNamespaces(PsiElement element) {
        return OMTMetaTreeUtil.collectMetaParents(element, YAMLMapping.class, OMTPrefixProvider.class, false, Objects::isNull)
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getNamespaces(entry.getKey()))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (s, s2) -> s));
    }


    @Override
    public String getModuleName() {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(getModuleFile(), YAMLMapping.class))
                .map(mapping -> mapping.getKeyValueByKey("moduleName"))
                .map(YAMLKeyValue::getValueText)
                .orElse(null);
    }

    @Override
    public OMTFile getModuleFile() {
        if (getName().contains(".module.omt")) {
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
