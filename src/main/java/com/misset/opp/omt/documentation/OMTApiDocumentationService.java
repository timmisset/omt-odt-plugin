package com.misset.opp.omt.documentation;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.misset.opp.documentation.ApiDocumentationService;
import com.misset.opp.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Service
public final class OMTApiDocumentationService extends ApiDocumentationService {

    public OMTApiDocumentationService(Project project) {
        super(project);
    }

    @Override
    protected @NotNull Path getPathToApiDocument() {
        return Path.of(SettingsState.getInstance(project).omtAPIPath);
    }

    public static OMTApiDocumentationService getInstance(@NotNull Project project) {
        return project.getService(OMTApiDocumentationService.class);
    }
}
