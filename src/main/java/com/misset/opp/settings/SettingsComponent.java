package com.misset.opp.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.misset.opp.settings.components.ModelInstanceMapperTable;
import com.misset.opp.settings.components.PathMapperTable;
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
    private final PathMapperTable pathMapperTable = new PathMapperTable();
    private final ModelInstanceMapperTable modelInstanceMapperTable = new ModelInstanceMapperTable();
    private final JBCheckBox resolveCallSignature = new JBCheckBox("Resolve call signature when input parameter is not typed");
    private final JBCheckBox applyQueryStepFilter = new JBCheckBox("Apply query step filter, disabled means all filters resolve to owl:Thing");

    public SettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(new JXTitledSeparator("Heavy features"))
                .addComponent(new JBLabel("Certain features can seriously impact the performance of the plugin"))
                .addComponent(resolveCallSignature)
                .addComponent(applyQueryStepFilter)
                .addComponent(new JXTitledSeparator("Ontology"))
                .addComponent(new JBLabel("Specify the root file of the OPP model"))
                .addComponent(new JBLabel("All ttl files in the same folder and subfolders are also read"))
                .addComponent(new JBLabel(
                        "The owl:imports in the root.ttl and all importing files will determine how the final ontology is loaded"))
                .addComponent(ontologyModelRootPath)
                .addComponent(new JBLabel("Known instances:"))
                .addComponent(new JBLabel(
                        "Add Regular expressions to register known IRIs as instances of specific model classes"))
                .addComponent(modelInstanceMapperTable.getComponent())
                .addComponent(new JXTitledSeparator("Mapping"))
                .addComponent(new JBLabel(
                        "Add mapped imports to specific locations in the project. Make sure to also add these in the tsconfig.json file"))
                .addComponent(pathMapperTable.getComponent())
                .addComponent(new JBLabel(
                        "The paths are evaluated on longest-first. The sorting by name in this table is for your convenience."))
                .addComponent(new JXTitledSeparator("Reasons"))
                .addComponent(reasonsRoot)
                .getPanel();
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
    public Map<String, String> getPathMapper() {
        return pathMapperTable.getTableView()
                .getItems()
                .stream()
                .collect(Collectors.toMap(
                        PathMapperTable.Item::getName,
                        PathMapperTable.Item::getPath
                ));
    }

    public void setPathMapper(Map<String, String> entries) {
        final List<PathMapperTable.Item> values = entries.entrySet().stream().map(
                        entry -> new PathMapperTable.Item(entry.getKey(), entry.getValue())
                )
                .sorted(Comparator.comparing(PathMapperTable.Item::getName))
                .collect(Collectors.toList());
        pathMapperTable.setValues(values);
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

    public void setApplyQueryStepFilter(boolean selected) {
        this.applyQueryStepFilter.setSelected(selected);
    }

    public boolean getApplyQueryStepFilter() {
        return applyQueryStepFilter.isSelected();
    }

    public void setResolveCallSignature(boolean selected) {
        this.resolveCallSignature.setSelected(selected);
    }

    public boolean getResolveCallSignature() {
        return resolveCallSignature.isSelected();
    }

}
