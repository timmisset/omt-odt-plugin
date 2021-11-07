package com.misset.opp.ttl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import org.jetbrains.annotations.NotNull;

public class LoadOntologyStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            // A slow task, such as loading the ontology should not claim the UI thread
            // instead, it should be run as background task
            Task.Backgroundable task = new Task.Backgroundable(project, "Loading OPP ontology") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    ReadAction.run(() -> {
                        FilenameIndex.getAllFilesByExt(project, "ttl")
                                .stream()
                                .filter(virtualFile -> virtualFile.getName().equals("root.ttl"))
                                .findFirst()
                                .ifPresent(this::loadOntology);
                    });

                }

                private void loadOntology(VirtualFile rootFile) {
                    new OppModelLoader().read(rootFile.toNioPath().toFile());
                }
            };

            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    task, new BackgroundableProcessIndicator(task)
            );
        });
    }




}
