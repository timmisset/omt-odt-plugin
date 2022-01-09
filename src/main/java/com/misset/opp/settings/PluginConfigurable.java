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

        // default path to the reasons folder
        state.reasonsFolder = project.getBasePath() + "/reasons";

        // some generic mappings
        final HashMap<String, String> pathMapping = new HashMap<>();
        pathMapping.put("@client", "frontend/libs");
        pathMapping.put("@activiteit", "frontend/libs/activiteit-domain");
        pathMapping.put("@forensische-opsporing", "frontend/libs/forensische-opsporing-domain");
        pathMapping.put("@generiek", "frontend/libs/generiek-domain");
        pathMapping.put("@handeling", "frontend/libs/handeling-domain");
        pathMapping.put("@main", "frontend/libs/main-domain");
        pathMapping.put("@mpg", "frontend/libs/mpg-domain");
        pathMapping.put("@registratie", "frontend/libs/registratie-domain");
        pathMapping.put("@shared", "frontend/libs/shared-domain");
        pathMapping.put("@signalering", "frontend/libs/signalering-domain");
        pathMapping.put("@werkopdracht", "frontend/libs/werkopdracht-domain");
        state.mappingPaths = pathMapping;

        final HashMap<String, String> modelInstanceMapping = new HashMap<>();
        modelInstanceMapping.put("http://data\\.politie\\.nl/19000000000000_\\S+",
                "http://ontologie.politie.nl/def/platform#NamedGraph");
        state.modelInstanceMapping = modelInstanceMapping;

        state.applyQueryStepFilter = true;
        state.resolveCallSignatures = true;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        SettingsState settingsState = getState();
        boolean modified = !settingsComponent.getOntologyModelRootPath().equals(settingsState.ontologyModelRootPath);
        modified |= settingsComponent.getApplyQueryStepFilter() != settingsState.applyQueryStepFilter;
        modified |= settingsComponent.getResolveCallSignature() != settingsState.resolveCallSignatures;
        modified |= isMappingPathsModified(settingsState);
        modified |= isModelInstanceMappingModified(settingsState);
        modified |= !settingsComponent.getReasonsRoot().equals(settingsState.reasonsFolder);
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
        saveOntologyState(settingsState);
        saveReasonsState(settingsState);
        saveMappingState(settingsState);
        saveModelInstanceMappingState(settingsState);
        settingsState.resolveCallSignatures = settingsComponent.getResolveCallSignature();
        settingsState.applyQueryStepFilter = settingsComponent.getApplyQueryStepFilter();
    }

    private void saveOntologyState(SettingsState settingsState) {
        if (!settingsState.ontologyModelRootPath.equals(settingsComponent.getOntologyModelRootPath())) {
            LoadOntologyStartupActivity.loadOntology(project);
        }
        settingsState.ontologyModelRootPath = settingsComponent.getOntologyModelRootPath();
    }

    private void saveReasonsState(SettingsState settingsState) {
        if (!settingsState.reasonsFolder.equals(settingsComponent.getReasonsRoot())) {
            LoadReasonsStartupActivity.loadReasons(project);
        }
        settingsState.reasonsFolder = settingsComponent.getReasonsRoot();
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
        settingsComponent.setPathMapper(settingsState.mappingPaths);
        settingsComponent.setModelInstanceMapper(settingsState.modelInstanceMapping);
        settingsComponent.setApplyQueryStepFilter(settingsState.applyQueryStepFilter);
        settingsComponent.setResolveCallSignature(settingsState.resolveCallSignatures);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
