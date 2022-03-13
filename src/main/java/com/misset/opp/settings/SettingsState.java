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
import com.misset.opp.ttl.OppModel;
import net.minidev.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public String ontologyModelRootPath = "";
    public String reasonsFolder = "";
    public String referencesFolder = "";
    public String tsConfigPath = "";
    public Map<String, String> modelInstanceMapping = new HashMap<>();
    public boolean referenceDetails = false;
    private Project project;

    public static SettingsState getInstance(Project project) {
        SettingsState settingsState = project.getService(SettingsState.class);
        initDefaultForNulls(settingsState, project);
        settingsState.project = project;
        return settingsState;
    }

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

        if (!settingsState.modelInstanceMapping.containsKey(NAMED_GRAPH_URI_PREFIX)) {
            settingsState.modelInstanceMapping.put(
                    NAMED_GRAPH_URI_PREFIX,
                    OppModel.INSTANCE.NAMED_GRAPH_CLASS.getURI()
            );
        }
    }

    public String getMappingPath(String path) {
        try {
            String[] parts = path.split("/");
            String domain = parts[0];
            String remainder = Arrays.stream(parts).skip(1).collect(Collectors.joining("/"));
            File file = new File(tsConfigPath);
            JSONArray array = JsonPath.parse(file).read("$..['paths']" + domain + "/*[0]");
            if (array.size() >= 1) {
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
        if (array == null || array.size() == 0) {
            return null;
        }
        String path = array.get(0).toString().replace("/*", "");
        Path parent = new File(tsConfigPath).toPath().toAbsolutePath().getParent();
        return Path.of(parent.toString() + "/" + path).toString();
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
    public void initializeComponent() {

    }
}
