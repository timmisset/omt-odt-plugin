package com.misset.opp.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SettingsComponent {

    private final JPanel myMainPanel;
    private final TextFieldWithBrowseButton ontologyModelRootPath = getFileLocationSetting("root.ttl");

    public SettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(new JXTitledSeparator("Ontology root"))
                .addLabeledComponent(new JBLabel("Ontology model root:"), ontologyModelRootPath, 1, true)
                .getPanel();
    }

    private TextFieldWithBrowseButton getFileLocationSetting(String name) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.setEditable(true);
        textFieldWithBrowseButton.addBrowseFolderListener(
                new TextBrowseFolderListener(getFileDescriptorByFilename(name)));
        return textFieldWithBrowseButton;
    }

    private FileChooserDescriptor getFileDescriptorByFilename(final String name) {
        return new FileChooserDescriptor(true, false, false, false, false, false).withFileFilter(
                file -> Comparing.equal(file.getName(), name, SystemInfo.isFileSystemCaseSensitive));
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    @NotNull
    public String getOntologyModelRootPath() {
        return ontologyModelRootPath.getText();
    }

    public void setOntologyModelRootPath(@NotNull String newText) {
        ontologyModelRootPath.setText(newText);
    }

}
