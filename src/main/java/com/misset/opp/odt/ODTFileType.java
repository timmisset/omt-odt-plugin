package com.misset.opp.odt;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ODTFileType extends LanguageFileType {

    public static final Icon ICON = IconLoader.findIcon("/icons/cartman-16.png", ODTFileType.class.getClassLoader());
    public static final ODTFileType INSTANCE = new ODTFileType();

    private ODTFileType() {
        super(ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "ODT File";
    }

    @Override
    public @NotNull String getDescription() {
        return "ODT Language file";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "odt";
    }

    @Override
    public @Nullable Icon getIcon() {
        return ICON;
    }

}
