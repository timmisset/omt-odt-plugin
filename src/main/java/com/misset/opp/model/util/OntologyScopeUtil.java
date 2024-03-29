package com.misset.opp.model.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OntologyScopeUtil {

    private OntologyScopeUtil() {
        // empty constructor
    }

    private static long modelVersion = -1;
    private static List<VirtualFile> virtualFiles = Collections.emptyList();

    public static List<VirtualFile> getTTLVirtualFiles(Project project) {
        long modificationCount = OntologyModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER.getModificationCount();
        if (modelVersion != modificationCount) {
            VirtualFileManager fileManager = VirtualFileManager.getInstance();
            virtualFiles = OntologyModelLoader.getInstance(project).getModelFiles().stream()
                    .map(file -> fileManager.findFileByNioPath(file.toPath()))
                    .collect(Collectors.toList());
            modelVersion = modificationCount;
        }
        return virtualFiles;
    }

}
