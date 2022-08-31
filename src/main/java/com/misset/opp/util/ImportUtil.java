package com.misset.opp.util;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ImportUtil {

    private ImportUtil() {
        // empty constructor
    }

    @Nullable
    public static VirtualFile getFile(String path) {
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

    @Nullable
    public static String getPathToFile(PsiElement from,
                                       PsiElement to) {
        PsiFile fromFile = from instanceof PsiFile ? (PsiFile) from : from.getContainingFile();
        PsiFile toFile = to instanceof PsiFile ? (PsiFile) to : to.getContainingFile();
        return getPathToFile(fromFile, toFile);
    }

    public static String getPathToFile(PsiFile from, PsiFile to) {
        if (from == null || to == null) {
            return null;
        }
        return getPathToFile(from.getVirtualFile(), to.getVirtualFile());
    }

    public static String getPathToFile(VirtualFile from, VirtualFile to) {
        if (from == null || to == null) {
            return null;
        }
        final String pathToFile = Path.of(from.getPath()).getParent()
                .relativize(Path.of(to.getPath()))
                .toString()
                .replace("\\", "/");
        return (!pathToFile.startsWith(".") ? "./" : "") + pathToFile;
    }

}
