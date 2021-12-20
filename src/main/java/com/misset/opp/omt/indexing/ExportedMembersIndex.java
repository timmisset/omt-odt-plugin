package com.misset.opp.omt.indexing;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.ImportUtil;
import com.misset.opp.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExportedMembersIndex {

    private static final HashMap<String, HashMap<String, List<PsiCallable>>> exportedMembers = new HashMap<>();
    private static final HashMap<String, List<PsiCallable>> exportedMembersByName = new HashMap<>();
    private static final Logger LOGGER = Logger.getInstance(ImportedMembersIndex.class);

    public static HashMap<String, List<PsiCallable>> getExportedMembers(String path,
                                                                        Project project) {
        if (exportedMembers.containsKey(path)) {
            return exportedMembers.get(path);
        } else {
            return calculateExportedMembers(path, project);
        }
    }

    public static void analyse(OMTFile file) {
        LoggerUtil.runWithLogger(LOGGER, "Analysis of " + file.getName(), () -> {
            String path = file.getVirtualFile().getPath();
            HashMap<String, List<PsiCallable>> exportingMembersMap = file.getDeclaredExportingMembersMap();
            exportedMembers.put(path, exportingMembersMap);
            exportingMembersMap.keySet().forEach(
                    s -> {
                        List<PsiCallable> paths = exportedMembersByName.getOrDefault(s, new ArrayList<>());
                        paths.addAll(exportingMembersMap.get(s));
                        exportedMembersByName.put(s, paths);
                    }
            );
        });
    }

    /**
     * Returns a list with locations where there is a PsiFile that contains the member
     */
    public static List<PsiCallable> getExportedMemberLocationsByName(String name) {
        List<PsiCallable> psiCallables = exportedMembersByName.getOrDefault(name, Collections.emptyList());
        // clean-up
        psiCallables.removeIf(psiCallable -> !psiCallable.isValid());
        return psiCallables;
    }

    public static void removeFromIndex(String path) {
        exportedMembers.remove(path);
    }

    public static void clear() {
        exportedMembers.clear();
        exportedMembersByName.clear();
    }

    private static HashMap<String, List<PsiCallable>> calculateExportedMembers(String path,
                                                                               Project project) {

        final OMTFile omtFile = ImportUtil.getOMTFile(path, project);
        if (omtFile == null) {
            return new HashMap<>();
        }
        final HashMap<String, List<PsiCallable>> exportingMembersMap = omtFile.getExportingMembersMap();
        exportedMembers.put(path, exportingMembersMap);
        return exportingMembersMap;
    }

}
