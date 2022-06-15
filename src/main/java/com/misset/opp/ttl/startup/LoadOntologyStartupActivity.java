package com.misset.opp.ttl.startup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.json.JsonFileType;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
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
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelLoader;
import com.misset.opp.ttl.util.TTLScopeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.jena.ontology.OntClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;

public class LoadOntologyStartupActivity implements StartupActivity.RequiredForSmartMode {
    private static Timer timer;

    /**
     * File change events can occur many times when a git branch is checked out
     * To prevent duplicate triggers of loading the model a git checkout receives a 5-second delay
     * to allow for more changes to be fetched in meantime
     */
    private static void scheduleTask(Task.Backgroundable task,
                                     ProgressIndicator progressIndicator,
                                     Project project) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationGroupManager.getInstance().getNotificationGroup("Update Ontology")
                        .createNotification(
                                "Change detected in TTL file that is part of the current model, entire model will be reloaded",
                                NotificationType.INFORMATION)
                        .setIcon(OMTFileType.INSTANCE.getIcon())
                        .notify(project);

                task.run(progressIndicator);
                timer.cancel();
            }
        }, 5000);
    }

    @Override
    public void runActivity(@NotNull Project project) {
        loadOntology(project);
    }

    public static void loadOntology(@NotNull Project project) {
        final Task.Backgroundable task = getBackgroundableTask(project);
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                task, new BackgroundableProcessIndicator(task)
        );
    }

    private static Task.Backgroundable getBackgroundableTask(Project project) {
        return new Task.Backgroundable(project, "Loading OPP Model") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setText("Loading OPP Ontology");
                getLoadOntologyTask(project).run(indicator);
                indicator.setText("Loading known instances");
                getKnownInstancesTask(project).run(indicator);
                indicator.setText("Loading OPP References");
                getLoadReferencesTask(project).run(indicator);
            }
        };
    }

    private static Task.Backgroundable getLoadOntologyTask(Project project) {
        return new Task.Backgroundable(project, "Loading OPP Ontology") {
            private VirtualFile getFileFromIndex(SettingsState state) {
                if (DumbService.isDumb(project)) {
                    return null;
                }
                return ReadAction.compute(() ->
                        FilenameIndex.getAllFilesByExt(project, "ttl")
                                .stream()
                                .filter(virtualFile -> virtualFile.getName().equals("root.ttl"))
                                .peek(virtualFile -> state.ontologyModelRootPath = virtualFile.getPath())
                                .findFirst()
                                .orElse(null)
                );
            }

            private VirtualFile getFileFromSettings(String ontologyModelRootPath) {
                try {
                    return VirtualFileManager.getInstance().findFileByNioPath(
                            Path.of(ontologyModelRootPath));
                } catch (Exception e) {
                    return null;
                }
            }

            private void showNotification() {
                // show warning that the file cannot be resolved:
                NotificationGroupManager.getInstance().getNotificationGroup("Update Ontology")
                        .createNotification(
                                "Could not find the Ontology, please provide the root.ttl location in the Settings",
                                NotificationType.ERROR)
                        .setIcon(OMTFileType.INSTANCE.getIcon())
                        .notify(project);
            }

            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                final SettingsState state = Optional.of(SettingsState.getInstance(project))
                        .map(SettingsState::getState)
                        .orElse(null);
                if (state == null) {
                    return;
                }

                final String ontologyModelRootPath = state.ontologyModelRootPath;
                VirtualFile file = ontologyModelRootPath.isBlank() ? getFileFromIndex(state) : getFileFromSettings(
                        ontologyModelRootPath);
                if (file != null) {
                    loadOntology(file);
                } else {
                    showNotification();
                }

                // subscribe to TTL file changes, this should reload the model
                project.getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
                    @Override
                    public void after(@NotNull List<? extends @NotNull VFileEvent> events) {
                        List<VirtualFile> ttlVirtualFiles = TTLScopeUtil.getTTLVirtualFiles();
                        if (events.stream().anyMatch(vFileEvent -> ttlVirtualFiles.contains(vFileEvent.getFile()))) {
                            // reload the model
                            scheduleTask(getBackgroundableTask(project), indicator, project);
                        }
                    }
                });
            }

            private void loadOntology(VirtualFile rootFile) {
                new OppModelLoader().read(rootFile.toNioPath().toFile());
            }
        };
    }

    protected static Task.Backgroundable getKnownInstancesTask(Project project) {
        return new Task.Backgroundable(project, "Loading known instances") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                SettingsState instance = SettingsState.getInstance(project);
                instance.knownInstances.forEach(
                        (instanceUri, typeUri) -> {
                            OntClass ontClass = OppModel.INSTANCE.getClass(typeUri);
                            if (ontClass != null) {
                                OppModel.INSTANCE.createIndividual(ontClass, instanceUri);
                            }
                        }
                );
            }
        };
    }

    protected static Task.Backgroundable getLoadReferencesTask(Project project) {
        return new Task.Backgroundable(project, "Loading OPP References") {
            @Override
            public void run(@Nullable ProgressIndicator indicator) {
                ReadAction.run(() -> loadReferences(project, indicator));
            }

            private void loadReferences(Project project, ProgressIndicator indicator) {
                JsonObject references = new JsonObject();
                getReferenceFiles(project)
                        .forEach(file -> processJson(file, references));
                SettingsState instance = SettingsState.getInstance(project);
                OppModel.INSTANCE.addFromJson(references, indicator, instance.referenceDetails);
            }

            private Collection<File> getReferenceFiles(Project project) {
                return getReferencesFolder(project).or(() -> findReferencesFolder(project))
                        .filter(VirtualFile::exists)
                        .map(VirtualFile::toNioPath)
                        .map(Path::toFile)
                        .map(folder -> FileUtils.listFiles(folder, new String[]{"json"}, true))
                        .orElse(Collections.emptyList());
            }

            private Optional<VirtualFile> getReferencesFolder(Project project) {
                return Optional.of(SettingsState.getInstance(project).referencesFolder)
                        .filter(s -> !s.isBlank())
                        .map(Path::of)
                        .map(VirtualFileManager.getInstance()::findFileByNioPath);
            }

            private Optional<VirtualFile> findReferencesFolder(Project project) {
                if (DumbService.isDumb(project)) {
                    return Optional.empty();
                }
                return FileTypeIndex
                        .getFiles(JsonFileType.INSTANCE, GlobalSearchScope.projectScope(project))
                        .stream()
                        .filter(virtualFile -> virtualFile.getPath().contains("/referentielijsten/"))
                        .map(VirtualFile::getParent)
                        .findFirst();
            }

            private void processJson(File file, JsonObject references) {
                try {
                    final JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
                    if (!jsonElement.isJsonObject()) {
                        return;
                    }
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("type") && jsonObject.has("triples")) {
                        String type = jsonObject.get("type").getAsString();
                        references.add(type, jsonObject.get("triples"));
                    }
                } catch (Exception ignored) {
                }
            }
        };
    }
}
