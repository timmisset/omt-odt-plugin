package com.misset.opp.ttl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import org.jetbrains.annotations.NotNull;

public class LoadOntologyStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        FilenameIndex.getAllFilesByExt(project, "ttl")
                .stream()
                .filter(virtualFile -> virtualFile.getName().equals("root.ttl"))
                .findFirst()
                .ifPresent(this::loadOntology);
    }

    private void loadOntology(VirtualFile rootFile) {
        new OppModelLoader().read(rootFile.toNioPath().toFile());
    }
}
