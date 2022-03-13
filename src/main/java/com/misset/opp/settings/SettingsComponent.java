package com.misset.opp.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import com.misset.opp.settings.components.ModelInstanceMapperTable;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsComponent {

    private final JPanel myMainPanel;
    private final TextFieldWithBrowseButton ontologyModelRootPath = getFileLocationSetting("root.ttl");
    private final TextFieldWithBrowseButton reasonsRoot = getFolderLocationSetting();
    private final TextFieldWithBrowseButton referencesRoot = getFolderLocationSetting();
    private final ModelInstanceMapperTable modelInstanceMapperTable = new ModelInstanceMapperTable();
    private final TextFieldWithBrowseButton tsconfig = getFileLocationSetting("*.json");
    private final JBCheckBox referenceDetails = new JBCheckBox("Include reference data");

    public SettingsComponent() {
        JBLabel ontologyRootLabel = new JBLabel("Specify the root file of the OPP model.<br>" +
                "All ttl files in the same folder and subfolders are also read.<br>" +
                "The owl:imports in the root.ttl and all importing files will determine how the final ontology is loaded.")
                .setAllowAutoWrapping(true)
                .setCopyable(true);
        JBLabel references = new JBLabel("Reference lists (instance data) can be included in the in-memory OPP model that the plugin uses.<br>" +
                "This will allow completion and model validation to understand explicitly used instances of the model in ODT " +
                "queries or statements.<br>By including the reference details, all the values are also loaded which is noticeable " +
                "when the IDE starts.<br>The values are displayed in the quick-documentation when you hover instance URIs.")
                .setAllowAutoWrapping(true)
                .setCopyable(true);
        JBLabel regexInstances = new JBLabel("Additional instances can be registered in the in-memory OPP model based on a Regular Expression.<br>" +
                "When an URI is not recognized it will be matched against the RegEx and added as known instance for as long as the IDE running.")
                .setAllowAutoWrapping(true)
                .setCopyable(true);

        JPanel panel = FormBuilder.createFormBuilder()
                .addComponent(new JBLabel("TSConfig file that contains path mappings:"))
                .addComponent(tsconfig)
                .addComponent(ontologyRootLabel)
                .addComponent(ontologyModelRootPath)
                .addComponent(new JXTitledSeparator("References"))
                .addComponent(references)
                .addComponent(new JBLabel("Select the root folder that contains .json files with references"))
                .addComponent(referencesRoot)
                .addComponent(referenceDetails)
                .addComponent(new JBLabel("Known instances:"))
                .addComponent(regexInstances)
                .addComponentFillVertically(modelInstanceMapperTable.getComponent(), UIUtil.DEFAULT_VGAP)
                .getPanel();

        myMainPanel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(panel, UIUtil.DEFAULT_VGAP).getPanel();
    }

    private TextFieldWithBrowseButton getFileLocationSetting(String name) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.setEditable(true);
        textFieldWithBrowseButton.addBrowseFolderListener(
                new TextBrowseFolderListener(getFileDescriptorByFilename(name)));
        return textFieldWithBrowseButton;
    }

    private TextFieldWithBrowseButton getFolderLocationSetting() {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.setEditable(true);
        textFieldWithBrowseButton.addBrowseFolderListener(
                new TextBrowseFolderListener(new FileChooserDescriptor(false, true, false, false, false, false)));
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

    @NotNull
    public String getReasonsRoot() {
        return reasonsRoot.getText();
    }

    public void setReasonsRoot(@NotNull String newText) {
        reasonsRoot.setText(newText);
    }

    @NotNull
    public String getReferencesRoot() {
        return referencesRoot.getText();
    }

    public void setReferencesRoot(@NotNull String newText) {
        referencesRoot.setText(newText);
    }

    @NotNull
    public String getTsConfigPath() {
        return tsconfig.getText();
    }

    public void setTsConfigPath(@NotNull String newText) {
        tsconfig.setText(newText);
    }

    @NotNull
    public Map<String, String> getModelInstanceMapper() {
        return modelInstanceMapperTable.getTableView()
                .getItems()
                .stream()
                .collect(Collectors.toMap(
                        ModelInstanceMapperTable.Item::getRegEx,
                        ModelInstanceMapperTable.Item::getOntologyClass
                ));
    }

    public void setModelInstanceMapper(Map<String, String> entries) {
        final List<ModelInstanceMapperTable.Item> values = entries.entrySet().stream().map(
                        entry -> new ModelInstanceMapperTable.Item(entry.getKey(), entry.getValue())
                )
                .sorted(Comparator.comparing(ModelInstanceMapperTable.Item::getRegEx))
                .collect(Collectors.toList());
        modelInstanceMapperTable.setValues(values);
    }

    public boolean getReferenceDetails() {
        return referenceDetails.isSelected();
    }

    public void setReferenceDetails(boolean selected) {
        referenceDetails.setSelected(selected);
    }
}
