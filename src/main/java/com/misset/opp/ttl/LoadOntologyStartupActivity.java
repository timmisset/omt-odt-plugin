package com.misset.opp.ttl;

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
import com.intellij.psi.search.FilenameIndex;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

public class LoadOntologyStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        loadOntology(project);
    }

    public static void loadOntology(@NotNull Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            // A slow task, such as loading the ontology should not claim the UI thread
            // instead, it should be run as background task

            final Task.Backgroundable task = getBackgroundableTask(project);
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    task, new BackgroundableProcessIndicator(task)
            );
        });
    }

    private static Task.Backgroundable getBackgroundableTask(Project project) {
        return new Task.Backgroundable(project, "Loading OPP ontology") {
            private VirtualFile getFileFromIndex(SettingsState state) {
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
                final SettingsState state = Optional.ofNullable(SettingsState.getInstance(project))
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
            }

            private void loadOntology(VirtualFile rootFile) {
                new OppModelLoader().read(rootFile.toNioPath().toFile());
            }
        };
    }

}
