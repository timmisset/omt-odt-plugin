package com.misset.opp.odt.documentation;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.misset.opp.documentation.ApiDocumentationService;
import com.misset.opp.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Service
public final class ODTApiDocumentationService extends ApiDocumentationService {

    public ODTApiDocumentationService(Project project) {
        super(project);
    }

    @Override
    protected @NotNull Path getPathToApiDocument() {
        return Path.of(SettingsState.getInstance(project).getOdtAPIPath());
    }

    public static ODTApiDocumentationService getInstance(@NotNull Project project) {
        return project.getService(ODTApiDocumentationService.class);
    }
}
