package com.misset.opp.omt.documentation;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public final class ApiDocumentationService {

    private final Project project;
    private Path pathToApiDocument;

    public ApiDocumentationService(Project project) {
        this.project = project;
    }

    public static ApiDocumentationService getInstance(@NotNull Project project) {
        return project.getService(ApiDocumentationService.class);
    }

    private Path getPath() {
        if (pathToApiDocument != null) {
            return pathToApiDocument;
        }
        if (DumbService.isDumb(project)) {
            return null;
        }
        pathToApiDocument = Arrays.stream(FilenameIndex.getFilesByName(project, "API.md", GlobalSearchScope.allScope(project)))
                .map(PsiFile::getVirtualFile)
                .filter(virtualFile -> virtualFile.getParent().getName().equals("omt"))
                .map(virtualFile -> Path.of(virtualFile.getPath()))
                .findFirst()
                .orElse(null);
        return pathToApiDocument;
    }

    public String readOMTApiDocumentation(String contentPath) {
        Path path = getPath();
        if (path == null) {
            return null;
        }
        try {
            ApiReader apiReader = new ApiReader(path);
            return apiReader.getDescription(contentPath);
        } catch (IOException e) {
            return null;
        }
    }
}
