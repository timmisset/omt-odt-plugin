package com.misset.opp.settings;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.ttl.model.OppModelConstants;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static final String NAMED_GRAPH_URI_PREFIX = "http:\\/\\/data\\.politie\\.nl\\/19000000000000_\\S*";
    private String ontologyModelRootPath = "";
    private String reasonsFolder = "";
    private String referencesFolder = "";
    private String tsConfigPath = "";
    private String omtAPIPath = "";
    private String odtAPIPath = "";
    private Map<String, String> modelInstanceMapping = new HashMap<>();
    private Map<String, String> knownInstances = new HashMap<>();
    private boolean referenceDetails = false;

    private static void initDefaultForNulls(SettingsState settingsState, Project project) {
        if (settingsState.referencesFolder.isBlank()) {
            settingsState.referencesFolder = project.getBasePath() + "/frontend/apps/app/src/assets/referentielijsten";
        }

        if (settingsState.ontologyModelRootPath.isBlank()) {
            settingsState.ontologyModelRootPath = project.getBasePath() + "/model/ttl/root.ttl";
        }

        if (settingsState.tsConfigPath.isBlank()) {
            settingsState.tsConfigPath = project.getBasePath() + "/frontend/tsconfig.base.json";
        }

        if (settingsState.omtAPIPath.isBlank()) {
            settingsState.omtAPIPath = project.getBasePath() + "/core/omt/API.md";
        }

        if (settingsState.odtAPIPath.isBlank()) {
            settingsState.odtAPIPath = project.getBasePath() + "/core/odt/API.md";
        }

        if (!settingsState.modelInstanceMapping.containsKey(NAMED_GRAPH_URI_PREFIX) &&
                OppModelConstants.getNamedGraphClass() != null) {
            settingsState.modelInstanceMapping.put(
                    NAMED_GRAPH_URI_PREFIX,
                    OppModelConstants.getNamedGraphClass().getURI()
            );
        }
    }

    public String getOntologyModelRootPath() {
        return ontologyModelRootPath;
    }

    public void setOntologyModelRootPath(String ontologyModelRootPath) {
        this.ontologyModelRootPath = ontologyModelRootPath;
    }

    public String getReasonsFolder() {
        return reasonsFolder;
    }

    public void setReasonsFolder(String reasonsFolder) {
        this.reasonsFolder = reasonsFolder;
    }

    public String getReferencesFolder() {
        return referencesFolder;
    }

    public void setReferencesFolder(String referencesFolder) {
        this.referencesFolder = referencesFolder;
    }

    public String getTsConfigPath() {
        return tsConfigPath;
    }

    public void setTsConfigPath(String tsConfigPath) {
        this.tsConfigPath = tsConfigPath;
    }

    public String getOmtAPIPath() {
        return omtAPIPath;
    }

    public void setOmtAPIPath(String omtAPIPath) {
        this.omtAPIPath = omtAPIPath;
    }

    public String getOdtAPIPath() {
        return odtAPIPath;
    }

    public void setOdtAPIPath(String odtAPIPath) {
        this.odtAPIPath = odtAPIPath;
    }

    public Map<String, String> getModelInstanceMapping() {
        return modelInstanceMapping;
    }

    public void setModelInstanceMapping(Map<String, String> modelInstanceMapping) {
        this.modelInstanceMapping = modelInstanceMapping;
    }

    public Map<String, String> getKnownInstances() {
        return knownInstances;
    }

    public void setKnownInstances(Map<String, String> knownInstances) {
        this.knownInstances = knownInstances;
    }

    public boolean getReferenceDetails() {
        return referenceDetails;
    }

    private Project project;

    public static SettingsState getInstance(Project project) {
        SettingsState settingsState = project.getService(SettingsState.class);
        initDefaultForNulls(settingsState, project);
        settingsState.project = project;
        return settingsState;
    }

    public void setReferenceDetails(boolean referenceDetails) {
        this.referenceDetails = referenceDetails;
    }

    public String getMappingPath(String path) {
        try {
            String[] parts = path.split("/");
            String domain = parts[0];
            String remainder = Arrays.stream(parts).skip(1).collect(Collectors.joining("/"));
            File file = new File(tsConfigPath);
            JSONArray array = JsonPath.parse(file).read("$..['paths']" + domain + "/*[0]");
            if (!array.isEmpty()) {
                String mappingPath = (String) array.get(0);
                return Path.of(file.getParent() + "/" + mappingPath.replace("/*", "") + "/" + remainder).toAbsolutePath().toString();
            }
            return null;
        } catch (IOException exception) {
            showNotification(exception.getMessage());
            return null;
        }
    }

    private void showNotification(String notification) {
        NotificationGroupManager.getInstance().getNotificationGroup("Import mapping")
                .createNotification(
                        notification,
                        NotificationType.INFORMATION)
                .setIcon(OMTFileType.INSTANCE.getIcon())
                .notify(project);
    }

    public String getShorthandPath(String fullPath) {
        File file = new File(tsConfigPath);
        try {
            LinkedHashMap<String, JSONArray> map = JsonPath.parse(file).read("$['compilerOptions']['paths']");
            HashMap<String, String> invertedMap = new HashMap<>();
            map.forEach((key, value) -> invertedMap.put(getInvertedMapPath(value), key.replace("/*", "")));
            return invertedMap.entrySet().stream()
                    .filter(entry -> fullPath.startsWith(entry.getKey()))
                    .map(entry -> fullPath.replace(entry.getKey(), entry.getValue()))
                    .findFirst()
                    .orElse(null);
        } catch (IOException exception) {
            showNotification(exception.getMessage());
        } catch (PathNotFoundException exception) {
            showNotification("Cannot travel expected json structure in " + tsConfigPath + ", expected is: compilerOptions.paths");
        } catch (Exception exception) {
            showNotification("Unexpected format in " + tsConfigPath);
        }
        return null;
    }

    private String getInvertedMapPath(JSONArray array) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        String path = array.get(0).toString().replace("/*", "");
        Path parent = new File(tsConfigPath).toPath().toAbsolutePath().getParent();
        return Path.of(parent.toString() + "/" + path).toString();
    }

    @Override
    public @NotNull SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public void initializeComponent() {
        // do nothing
    }
}
