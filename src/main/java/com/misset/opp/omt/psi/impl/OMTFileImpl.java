package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.meta.OMTFileMetaType;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    private YAMLMapping getRootMapping() {
        return Optional.ofNullable(getFirstChild())
                .filter(YAMLDocument.class::isInstance)
                .map(PsiElement::getFirstChild)
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .orElse(null);
    }

    @Override
    public HashMap<String, List<PsiElement>> getExportingMembersMap() {
        return Optional.ofNullable(getRootMapping())
                .map(this::getExportingMembersMap)
                .orElse(new HashMap<>());
    }
    private HashMap<String, List<PsiElement>> getExportingMembersMap(YAMLMapping yamlMapping) {
        return ReadAction.compute(() -> {
            final HashMap<String, List<PsiElement>> map = new OMTFileMetaType("OMTFile").getCallableMap(yamlMapping);
            final YAMLKeyValue model = yamlMapping.getKeyValueByKey("model");
            if(model != null && model.getValue() instanceof YAMLMapping) {
                map.putAll(new OMTModelMetaType().getCallableMap((YAMLMapping) model.getValue()));
            }
            return map;
        });
    }
}
