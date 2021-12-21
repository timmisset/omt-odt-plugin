package com.misset.opp.util;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
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

}
