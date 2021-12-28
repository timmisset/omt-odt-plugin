package com.misset.opp.omt.startup;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IndexOMTStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            Task.Backgroundable task = getIndexingTask(project);

            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    task, new BackgroundableProcessIndicator(task)
            );
        });
    }

    public static Task.Backgroundable getIndexingTask(Project project) {
        return new Task.Backgroundable(project, "Indexing OMT Imports") {
            @Override
            public void run(@Nullable ProgressIndicator indicator) {
                ReadAction.run(() -> runIndexer(indicator, project));
            }
        };
    }

    private static void runIndexer(@Nullable ProgressIndicator indicator,
                                   Project project) {
        if (DumbService.getInstance(project).isDumb()) {
            return;
        }
        final PsiManager psiManager = PsiManager.getInstance(project);
        if (indicator != null) {
            indicator.setText("Indexing OMT Files");
        }
        FilenameIndex.getAllFilesByExt(project, "omt")
                .stream()
                .map(psiManager::findFile)
                .filter(OMTFile.class::isInstance)
                .map(OMTFile.class::cast)
                .forEach(omtFile -> {
                    if (indicator != null) {
                        indicator.setText2(omtFile.getName());
                    }
                    OMTPrefixIndex.analyse(omtFile);
                    OMTImportedMembersIndex.analyse(omtFile);
                });
        OMTPrefixIndex.orderIndexByFrequency();
    }
}
