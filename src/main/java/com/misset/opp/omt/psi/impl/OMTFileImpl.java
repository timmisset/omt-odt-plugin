package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.OMTLanguage;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.meta.OMTFileMetaType;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    private static final Key<CachedValue<HashMap<String, List<PsiElement>>>> EXPORTING_MEMBERS = new Key<>("EXPORTING_MEMBERS");
    private static final Key<CachedValue<Collection<OMTFile>>> IMPORTED_BY = new Key<>("IMPORTED_BY");

    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    private YAMLMapping getRootMapping() {
        return ReadAction.compute(() -> Optional.ofNullable(PsiTreeUtil.findChildOfType(this, YAMLDocument.class))
                .map(yamlDocument -> PsiTreeUtil.findChildOfType(yamlDocument, YAMLMapping.class))
                .orElse(null));
    }

    /**
     * Returns a list with all files that import this file:
     */
    public Collection<OMTFile> getImportedBy() {
        return CachedValuesManager.getCachedValue(this, IMPORTED_BY, () -> {
            final Collection<OMTFile> all = ReferencesSearch.search(this)
                    .allowParallelProcessing()
                    .mapping(PsiReference::getElement)
                    .mapping(PsiElement::getContainingFile)
                    .filtering(OMTFile.class::isInstance)
                    .mapping(OMTFile.class::cast)
                    .findAll();
            return new CachedValueProvider.Result<>(all, OMTLanguage.getLanguageModificationTracker(getProject()));
        });
    }

    @Override
    public GlobalSearchScope getMemberUsageScope(boolean isExportable) {
        List<OMTFile> files = new ArrayList<>();
        files.add(this);
        if (isExportable) {
            files.addAll(getImportedBy());
        }

        return GlobalSearchScope.filesScope(getProject(),
                files.stream().map(PsiFile::getVirtualFile).collect(
                        Collectors.toSet()));
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
        return ReadAction.compute(() -> {
            final HashMap<String, List<PsiElement>> map = new OMTFileMetaType("OMTFile").getCallableMap(yamlMapping);
            final YAMLKeyValue model = yamlMapping.getKeyValueByKey("model");
            if (model != null && model.getValue() instanceof YAMLMapping) {
                map.putAll(new OMTModelMetaType().getCallableMap((YAMLMapping) model.getValue()));
            }
            return map;
        });
    }

    @Override
    public void clearCaches() {
        super.clearCaches();
        Optional.ofNullable(getVirtualFile())
                .map(VirtualFile::getPath)
                .ifPresent(ExportedMembersIndex::removeFromIndex);
    }
}
