package com.misset.opp.documentation;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public abstract class ApiDocumentationService {

    protected Project project;

    protected ApiDocumentationService(Project project) {
        this.project = project;
    }

    protected abstract @NotNull Path getPathToApiDocument();

    public String readApiDocumentation(String contentPath) {
        Path path = getPathToApiDocument();
        if (!path.toFile().exists()) {
            throw new RuntimeException("Could not find or process API.md, please correct this in the plugin settings\n" +
                    "Provided value was: " + path);
        }
        try {
            ApiMarkdownReader apiMarkdownReader = new ApiMarkdownReader(path);
            return apiMarkdownReader.getDescription(contentPath);
        } catch (IOException e) {
            return null;
        }
    }
}
