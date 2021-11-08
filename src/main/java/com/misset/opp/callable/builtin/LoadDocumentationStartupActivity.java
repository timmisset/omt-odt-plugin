package com.misset.opp.callable.builtin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class LoadDocumentationStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        BuiltinDocumentationService.getInstance(project).loadDocuments();
    }




}
