package com.misset.opp.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ImportUtil {

    public @Nullable static VirtualFile getFile(String path) {
        if (path == null) {
            return null;
        }
        final VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
        final VirtualFile virtualFile;
        if (path.startsWith("temp:///")) {
            virtualFile = virtualFileManager.findFileByUrl(path);
        } else {
            virtualFile = virtualFileManager.findFileByNioPath(
                    Path.of(path)
            );
        }
        return virtualFile;
    }
    public static OMTFile getOMTFile(String path, Project project) {
        System.out.println("Checking out imported file: " + path);
        final VirtualFile file = getFile(path);
        if(file == null) { return null; }
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile instanceof OMTFile) {
            return (OMTFile) psiFile;
        } else {
            // do not throw class-cast exception, instead annotate the import path that it's the wrong format
            return null;
        }
    }

}
