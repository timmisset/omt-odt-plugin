package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.indexing.ImportedMembersIndex;
import com.misset.opp.omt.meta.OMTFileMetaType;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.*;
import java.util.stream.Collectors;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    private static final Key<CachedValue<HashMap<String, List<PsiElement>>>> EXPORTING_MEMBERS = new Key<>(
            "EXPORTING_MEMBERS");

    Logger LOGGER = Logger.getInstance(OMTFileImpl.class);

    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    private YAMLMapping getRootMapping() {
        return LoggerUtil.computeWithLogger(LOGGER, "getRootMapping", () ->
                ReadAction.compute(() -> Optional.ofNullable(PsiTreeUtil.findChildOfType(this, YAMLDocument.class))
                        .map(yamlDocument -> PsiTreeUtil.findChildOfType(yamlDocument, YAMLMapping.class))
                        .orElse(null)));

    }

    @Override
    public HashMap<String, List<PsiElement>> getExportingMembersMap() {
        return CachedValuesManager.getCachedValue(this, EXPORTING_MEMBERS, () -> {
            final HashMap<String, List<PsiElement>> map = Optional.ofNullable(getRootMapping())
                    .map(this::getExportingMembersMap)
                    .orElse(new HashMap<>());
            return new CachedValueProvider.Result<>(map, this);
        });
    }

    private HashMap<String, List<PsiElement>> getExportingMembersMap(YAMLMapping yamlMapping) {
        return LoggerUtil.computeWithLogger(LOGGER, "getExportingMembersMap", () ->
                ReadAction.compute(() -> {
                    final HashMap<String, List<PsiElement>> map = new OMTFileMetaType("OMTFile").getCallableMap(yamlMapping);
                    final YAMLKeyValue model = yamlMapping.getKeyValueByKey("model");
                    if (model != null && model.getValue() instanceof YAMLMapping) {
                        map.putAll(new OMTModelMetaType().getCallableMap((YAMLMapping) model.getValue()));
                    }
                    return map;
                }));
    }

    @Override
    public void clearCaches() {
        super.clearCaches();
        ImportedMembersIndex.analyse(this);
        Optional.ofNullable(getVirtualFile())
                .map(VirtualFile::getPath)
                .ifPresent(ExportedMembersIndex::removeFromIndex);
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
}
