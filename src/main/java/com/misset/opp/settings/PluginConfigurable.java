package com.misset.opp.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.misset.opp.ttl.LoadOntologyStartupActivity;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginConfigurable implements Configurable {
    private SettingsComponent settingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "OMT-ODT-Plugin Settings";
    }

    Project project;

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
        return settingsComponent.getPanel();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        SettingsState settingsState = SettingsState.getInstance(project);
        return !settingsComponent.getOntologyModelRootPath().equals(settingsState.ontologyModelRootPath);
    }

    @Override
    public void apply() {
        SettingsState settingsState = SettingsState.getInstance(project);
        if (!settingsState.ontologyModelRootPath.equals(settingsComponent.getOntologyModelRootPath())) {
            LoadOntologyStartupActivity.loadOntology(project);
        }
        settingsState.ontologyModelRootPath = settingsComponent.getOntologyModelRootPath();
    }

    @Override
    public void reset() {
        SettingsState settingsState = SettingsState.getInstance(project);
        settingsComponent.setOntologyModelRootPath(settingsState.ontologyModelRootPath);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
