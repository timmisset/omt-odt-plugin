package com.misset.opp.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.misset.opp.ttl.LoadOntologyStartupActivity;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PluginConfigurable implements Configurable {
    private SettingsComponent settingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "OMT-ODT-Plugin Settings";
    }

    Project project;

    private SettingsState getState() {
        return SettingsState.getInstance(project);
    }

    public PluginConfigurable(Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Configure it, so it works!";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new SettingsComponent();
        final SettingsState state = getState();
        if (state.useDefaultSettings()) {
            initWithDefaults();
        }
        reset();
        final JPanel panel = settingsComponent.getPanel();
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }

    private void initWithDefaults() {
        final SettingsState state = getState();
        // default path to the model
        state.ontologyModelRootPath = project.getBasePath() + "/model/ttl/root.ttl";

        // some generic mappings
        final HashMap<String, String> pathMapping = new HashMap<>();
        pathMapping.put("@client", "frontend/libs");
        pathMapping.put("@registratie", "frontend/libs/registratie-domain");
        pathMapping.put("@activiteiten", "frontend/libs/activiteiten-domain");
        pathMapping.put("@generiek", "frontend/libs/generiek-domain");
        pathMapping.put("@signalering", "frontend/libs/signalering-domain");
        state.mappingPaths = pathMapping;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        SettingsState settingsState = getState();
        boolean modified = !settingsComponent.getOntologyModelRootPath().equals(settingsState.ontologyModelRootPath);
        modified |= isMappingPathsModified(settingsState);
        return modified;
    }

    private boolean isMappingPathsModified(SettingsState settingsState) {
        final Map<String, String> mappingPaths = settingsState.mappingPaths;
        if (settingsComponent.getPathMapper().size() != mappingPaths.size()) {
            return true;
        }
        return settingsComponent.getPathMapper().entrySet()
                .stream()
                .anyMatch(entry ->
                        !mappingPaths.containsKey(entry.getKey()) || !mappingPaths.get(entry.getKey())
                                .equals(entry.getValue()));
    }

    @Override
    public void apply() {
        SettingsState settingsState = getState();
        saveOntologyState(settingsState);
        saveMappingState(settingsState);
    }

    private void saveOntologyState(SettingsState settingsState) {
        if (!settingsState.ontologyModelRootPath.equals(settingsComponent.getOntologyModelRootPath())) {
            LoadOntologyStartupActivity.loadOntology(project);
        }
        settingsState.ontologyModelRootPath = settingsComponent.getOntologyModelRootPath();
    }

    private void saveMappingState(SettingsState settingsState) {
        final HashMap<String, String> mapping = new HashMap<>();
        for (Map.Entry<String, String> entry : settingsComponent.getPathMapper().entrySet()) {
            if (!entry.getKey().isBlank() && !entry.getValue().isBlank()) {
                mapping.put(entry.getKey(), entry.getValue());
            }
        }
        settingsComponent.setPathMapper(mapping);
        settingsState.mappingPaths = mapping;
    }

    @Override
    public void reset() {
        SettingsState settingsState = getState();
        settingsComponent.setOntologyModelRootPath(settingsState.ontologyModelRootPath);
        settingsComponent.setPathMapper(settingsState.mappingPaths);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
