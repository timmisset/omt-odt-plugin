package com.misset.opp.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.misset.opp.settings.SettingsState",
        storages = {@Storage("omt-odt-plugin-settings.xml")}
)
@Service
public final class SettingsState implements PersistentStateComponent<SettingsState> {

    public String ontologyModelRootPath = "";
    public Map<String, String> mappingPaths = new HashMap<>();
    public Map<String, String> modelInstanceMapping = new HashMap<>();
    private boolean useDefaultSettings = false;

    public static SettingsState getInstance(Project project) {
        return project.getService(SettingsState.class);
    }

    @Nullable
    @Override
    public SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public void noStateLoaded() {
        useDefaultSettings = true;
    }

    public boolean useDefaultSettings() {
        return useDefaultSettings;
    }

    @Override
    public void initializeComponent() {

    }
}
