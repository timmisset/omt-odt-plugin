package com.misset.opp.omt;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.misset.opp.util.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class OMTFileType extends LanguageFileType {
    public static final OMTFileType INSTANCE = new OMTFileType();

    private OMTFileType() {
        super(OMTLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "OMT File";
    }

    @Override
    public @NotNull String getDescription() {
        return "OMT Language file, an extension of the YAML language file";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "omt";
    }

    @Override
    public @Nullable Icon getIcon() {
        return Icons.PLUGIN_ICON;
    }
}

