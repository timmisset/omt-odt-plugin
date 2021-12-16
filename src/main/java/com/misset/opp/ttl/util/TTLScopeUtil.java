package com.misset.opp.ttl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.OppModelLoader;
import com.misset.opp.ttl.psi.TTLFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TTLScopeUtil {
    private static long modelVersion = -1;
    private static List<VirtualFile> virtualFiles = Collections.emptyList();

    public static GlobalSearchScope getModelSearchScope(Project project) {
        return GlobalSearchScope.filesScope(project, getTTLVirtualFiles(project));
    }

    public static List<VirtualFile> getTTLVirtualFiles(Project project) {
        if (modelVersion != OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER.getModificationCount()) {
            VirtualFileManager fileManager = VirtualFileManager.getInstance();
            virtualFiles = OppModelLoader.getModelFiles().stream()
                    .map(file -> fileManager.findFileByNioPath(file.toPath()))
                    .collect(Collectors.toList());
            modelVersion = OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER.getModificationCount();
        }
        return virtualFiles;
    }

    public static List<TTLFile> getTTLFiles(Project project) {
        PsiManager psiManager = PsiManager.getInstance(project);
        return getTTLVirtualFiles(project).stream()
                .map(psiManager::findFile)
                .filter(TTLFile.class::isInstance)
                .map(TTLFile.class::cast)
                .collect(Collectors.toList());
    }

}
