package com.misset.opp.ttl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.OppModelLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TTLScopeUtil {
    private static long modelVersion = -1;
    private static List<VirtualFile> virtualFiles = Collections.emptyList();

    public static GlobalSearchScope getModelSearchScope(Project project) {
        return GlobalSearchScope.filesScope(project, getTTLVirtualFiles());
    }

    public static List<VirtualFile> getTTLVirtualFiles() {
        long modificationCount = OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER.getModificationCount();
        if (modelVersion != modificationCount) {
            VirtualFileManager fileManager = VirtualFileManager.getInstance();
            virtualFiles = OppModelLoader.getModelFiles().stream()
                    .map(file -> fileManager.findFileByNioPath(file.toPath()))
                    .collect(Collectors.toList());
            modelVersion = modificationCount;
        }
        return virtualFiles;
    }

}
