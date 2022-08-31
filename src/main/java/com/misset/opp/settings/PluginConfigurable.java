package com.misset.opp.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.misset.opp.omt.startup.LoadReasonsStartupActivity;
import com.misset.opp.ttl.startup.LoadOntologyStartupActivity;
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
        reset();
        final JPanel panel = settingsComponent.getPanel();
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        SettingsState settingsState = getState();
        boolean modified = !settingsComponent.getOntologyModelRootPath().equals(settingsState.getOntologyModelRootPath());
        modified |= isModelInstanceMappingModified(settingsState);
        modified |= !settingsComponent.getReasonsRoot().equals(settingsState.getReasonsFolder());
        modified |= !settingsComponent.getReferencesRoot().equals(settingsState.getReferencesFolder());
        modified |= !settingsComponent.getTsConfigPath().equals(settingsState.getTsConfigPath());
        modified |= !settingsComponent.getODTAPIPath().equals(settingsState.getOdtAPIPath());
        modified |= !settingsComponent.getOMTAPIPath().equals(settingsState.getOmtAPIPath());
        modified |= settingsComponent.getReferenceDetails() != settingsState.getReferenceDetails();
        return modified;
    }

    private boolean isModelInstanceMappingModified(SettingsState settingsState) {
        final Map<String, String> modelInstanceMapping = settingsState.getModelInstanceMapping();
        if (settingsComponent.getModelInstanceMapper().size() != modelInstanceMapping.size()) {
            return true;
        }
        return settingsComponent.getModelInstanceMapper().entrySet()
                .stream()
                .anyMatch(entry ->
                        !modelInstanceMapping.containsKey(entry.getKey()) || !modelInstanceMapping.get(entry.getKey())
                                .equals(entry.getValue()));
    }

    @Override
    public void apply() {
        SettingsState settingsState = getState();
        saveOntologyAndReferencesState(settingsState);
        saveReasonsState(settingsState);
        savePaths(settingsState);
        saveModelInstanceMappingState(settingsState);
    }


    /**
     * Since both a change in the model path and the references path require a full model reload
     * this method should run combined
     */
    private void saveOntologyAndReferencesState(SettingsState settingsState) {
        boolean reloadOntology = !settingsState.getOntologyModelRootPath().equals(settingsComponent.getOntologyModelRootPath());
        if (!settingsState.getReferencesFolder().equals(settingsComponent.getReferencesRoot())) {
            reloadOntology = true;
        }
        if (settingsState.getReferenceDetails() != settingsComponent.getReferenceDetails() &&
                settingsComponent.getReferenceDetails()) {
            reloadOntology = true;
        }
        settingsState.setReferencesFolder(settingsComponent.getReferencesRoot());
        settingsState.setOntologyModelRootPath(settingsComponent.getOntologyModelRootPath());
        settingsState.setReferenceDetails(settingsComponent.getReferenceDetails());
        if (reloadOntology) {
            LoadOntologyStartupActivity.loadOntology(project);
        }
    }

    private void saveReasonsState(SettingsState settingsState) {
        boolean reload = !settingsState.getReasonsFolder().equals(settingsComponent.getReasonsRoot());
        settingsState.setReasonsFolder(settingsComponent.getReasonsRoot());

        if (reload) {
            LoadReasonsStartupActivity.initReasons(project);
        }
    }

    private void savePaths(SettingsState settingsState) {
        settingsState.setTsConfigPath(settingsComponent.getTsConfigPath());
        settingsState.setOmtAPIPath(settingsComponent.getOMTAPIPath());
        settingsState.setOdtAPIPath(settingsComponent.getODTAPIPath());
    }

    private void saveModelInstanceMappingState(SettingsState settingsState) {
        final HashMap<String, String> mapping = new HashMap<>();
        for (Map.Entry<String, String> entry : settingsComponent.getModelInstanceMapper().entrySet()) {
            if (!entry.getKey().isBlank() && !entry.getValue().isBlank()) {
                mapping.put(entry.getKey(), entry.getValue());
            }
        }
        settingsComponent.setModelInstanceMapper(mapping);
        settingsState.setModelInstanceMapping(mapping);
    }

    @Override
    public void reset() {
        SettingsState settingsState = getState();
        settingsComponent.setOntologyModelRootPath(settingsState.getOntologyModelRootPath());
        settingsComponent.setReasonsRoot(settingsState.getReasonsFolder());
        settingsComponent.setReferencesRoot(settingsState.getReferencesFolder());
        settingsComponent.setModelInstanceMapper(settingsState.getModelInstanceMapping());
        settingsComponent.setReferenceDetails(settingsState.getReferenceDetails());
        settingsComponent.setTsConfigPath(settingsState.getTsConfigPath());
        settingsComponent.setODTAPIPath(settingsState.getOdtAPIPath());
        settingsComponent.setOMTAPIPath(settingsState.getOmtAPIPath());
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
