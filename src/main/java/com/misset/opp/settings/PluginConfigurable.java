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
        boolean modified = !settingsComponent.getOntologyModelRootPath().equals(settingsState.ontologyModelRootPath);
        modified |= isModelInstanceMappingModified(settingsState);
        modified |= !settingsComponent.getReasonsRoot().equals(settingsState.reasonsFolder);
        modified |= !settingsComponent.getReferencesRoot().equals(settingsState.referencesFolder);
        modified |= !settingsComponent.getTsConfigPath().equals(settingsState.tsConfigPath);
        modified |= settingsComponent.getReferenceDetails() != settingsState.referenceDetails;
        return modified;
    }

    private boolean isModelInstanceMappingModified(SettingsState settingsState) {
        final Map<String, String> modelInstanceMapping = settingsState.modelInstanceMapping;
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
        saveTsConfigPath(settingsState);
        saveModelInstanceMappingState(settingsState);
    }


    /**
     * Since both a change in the model path and the references path require a full model reload
     * this method should run combined
     */
    private void saveOntologyAndReferencesState(SettingsState settingsState) {
        boolean reloadOntology = false;
        if (!settingsState.ontologyModelRootPath.equals(settingsComponent.getOntologyModelRootPath())) {
            reloadOntology = true;
        }
        if (!settingsState.referencesFolder.equals(settingsComponent.getReferencesRoot())) {
            reloadOntology = true;
        }
        if (settingsState.referenceDetails != settingsComponent.getReferenceDetails() &&
                settingsComponent.getReferenceDetails()) {
            reloadOntology = true;
        }
        settingsState.referencesFolder = settingsComponent.getReferencesRoot();
        settingsState.ontologyModelRootPath = settingsComponent.getOntologyModelRootPath();
        settingsState.referenceDetails = settingsComponent.getReferenceDetails();
        if (reloadOntology) {
            LoadOntologyStartupActivity.loadOntology(project);
        }
    }

    private void saveReasonsState(SettingsState settingsState) {
        boolean reload = !settingsState.reasonsFolder.equals(settingsComponent.getReasonsRoot());
        settingsState.reasonsFolder = settingsComponent.getReasonsRoot();

        if (reload) {
            LoadReasonsStartupActivity.loadReasons(project);
        }
    }

    private void saveTsConfigPath(SettingsState settingsState) {
        settingsState.tsConfigPath = settingsComponent.getTsConfigPath();
    }

    private void saveModelInstanceMappingState(SettingsState settingsState) {
        final HashMap<String, String> mapping = new HashMap<>();
        for (Map.Entry<String, String> entry : settingsComponent.getModelInstanceMapper().entrySet()) {
            if (!entry.getKey().isBlank() && !entry.getValue().isBlank()) {
                mapping.put(entry.getKey(), entry.getValue());
            }
        }
        settingsComponent.setModelInstanceMapper(mapping);
        settingsState.modelInstanceMapping = mapping;
    }

    @Override
    public void reset() {
        SettingsState settingsState = getState();
        settingsComponent.setOntologyModelRootPath(settingsState.ontologyModelRootPath);
        settingsComponent.setReasonsRoot(settingsState.reasonsFolder);
        settingsComponent.setReferencesRoot(settingsState.referencesFolder);
        settingsComponent.setModelInstanceMapper(settingsState.modelInstanceMapping);
        settingsComponent.setReferenceDetails(settingsState.referenceDetails);
        settingsComponent.setTsConfigPath(settingsState.tsConfigPath);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
