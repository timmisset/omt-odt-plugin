package com.misset.opp.ttl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TTLFileType extends LanguageFileType {
    public static final TTLFileType INSTANCE = new TTLFileType();

    private TTLFileType() {
        super(TTLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Turtle File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Turtle language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "ttl";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return com.misset.opp.util.Icons.TTLFile;
    }
}

