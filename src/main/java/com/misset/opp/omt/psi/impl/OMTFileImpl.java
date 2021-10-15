package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;

public class OMTFileImpl extends YAMLFileImpl implements OMTFile {
    public OMTFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OMTFileType.INSTANCE;
    }

}
