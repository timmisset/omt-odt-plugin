package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.model.OMTModelBlock;
import com.misset.opp.omt.psi.impl.model.OMTModelBlockImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

import java.util.function.Function;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

    @Override
    @Nullable
    public OMTModelBlock getModelBlock() {
        return wrapKeyValue("model", OMTModelBlockImpl::new);
    }


    private <T extends PsiElement> T wrapKeyValue(String rootEntry, Function<YAMLKeyValue, T> wrapper) {
        final YAMLKeyValue keyValue = getRootEntry(rootEntry);
        if(keyValue == null) { return null; }
        return wrapper.apply(keyValue);
    }

    @Nullable
    private YAMLKeyValue getRootEntry(String entry) {
        return getDocuments()
                .stream()
                .map(YAMLDocument::getTopLevelValue)
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .map(yamlMapping -> yamlMapping.getKeyValueByKey(entry))
                .findFirst()
                .orElse(null);
    }
}
