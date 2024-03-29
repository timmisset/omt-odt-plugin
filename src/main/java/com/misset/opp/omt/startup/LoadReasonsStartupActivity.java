package com.misset.opp.omt.startup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.misset.opp.settings.SettingsState;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;

public class LoadReasonsStartupActivity implements StartupActivity {
    private static final HashMap<String, String> reasons = new HashMap<>();

    public static Map<String, String> getReasons() {
        return reasons;
    }

    public static void initReasons(Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            // A slow task, such as loading the ontology should not claim the UI thread
            // instead, it should be run as background task

            final Task.Backgroundable task = getBackgroundableTask(project);
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    task, new BackgroundableProcessIndicator(task)
            );
        });
    }

    private static void loadReasons(Project project) {
        reasons.clear();
        getReasonFiles(project).stream()
                .map(LoadReasonsStartupActivity::parseJson)
                .forEach(reasons::putAll);
    }

    protected static Task.Backgroundable getBackgroundableTask(Project project) {
        return new Task.Backgroundable(project, "Loading Transaction reasons") {
            @Override
            public void run(@Nullable ProgressIndicator indicator) {
                ReadAction.run(() -> loadReasons(project));
            }
        };
    }

    private static Collection<File> getReasonFiles(Project project) {
        return getReasonFolder(project).or(() -> findReasonFolder(project))
                .filter(VirtualFile::exists)
                .map(VirtualFile::toNioPath)
                .map(Path::toFile)
                .map(folder -> FileUtils.listFiles(folder, new String[]{"json"}, true))
                .orElse(Collections.emptyList());
    }

    private static Optional<VirtualFile> getReasonFolder(Project project) {
        return Optional.of(SettingsState.getInstance(project).getReasonsFolder())
                .filter(s -> !s.isBlank())
                .map(Path::of)
                .map(VirtualFileManager.getInstance()::findFileByNioPath);
    }

    private static Optional<VirtualFile> findReasonFolder(Project project) {
        return FileTypeIndex
                .getFiles(JsonFileType.INSTANCE, GlobalSearchScope.projectScope(project))
                .stream()
                .filter(virtualFile -> virtualFile.getPath().contains("/reasons/"))
                .map(VirtualFile::getParent)
                .findFirst();
    }

    private static HashMap<String, String> parseJson(File file) {
        HashMap<String, String> fileReasons = new HashMap<>();
        try {
            final JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
            if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("reasons")) {
                final JsonElement reasons = jsonElement.getAsJsonObject().get("reasons");
                if (reasons.isJsonArray()) {
                    reasons.getAsJsonArray().forEach(element -> addToMap(element, fileReasons));
                }
            }
        } catch (Exception ignored) {
            // ignored
        }
        return fileReasons;
    }

    private static void addToMap(JsonElement element,
                                 HashMap<String, String> map) {
        if (element.isJsonObject()) {
            final JsonObject asJsonObject = element.getAsJsonObject();
            if (!asJsonObject.has("reason")) {
                return;
            }
            String reason = asJsonObject.get("reason").getAsString();
            String description = asJsonObject.has("description") ?
                    asJsonObject.get("description").getAsString() :
                    "no description";
            map.put(reason, description);
        }
    }

    @Override
    public void runActivity(@NotNull Project project) {
        initReasons(project);
    }

}
