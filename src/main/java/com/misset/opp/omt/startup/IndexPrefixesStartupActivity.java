package com.misset.opp.omt.startup;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import org.jetbrains.annotations.NotNull;

public class IndexPrefixesStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            // A slow task, such as loading the ontology should not claim the UI thread
            // instead, it should be run as background task

            Task.Backgroundable task = OMTPrefixIndex.getIndexTask(project);

            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    task, new BackgroundableProcessIndicator(task)
            );
        });
    }
}
