package com.misset.opp.omt.indexing;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.ImportUtil;

import java.util.HashMap;
import java.util.List;

public class ExportedMembersIndex {

    private static final HashMap<String, HashMap<String, List<PsiElement>>> exportedMembers = new HashMap<>();
    private static final HashMap<String, List<String>> exportedMembersByName = new HashMap<>();

    public static HashMap<String, List<PsiElement>> getExportedMembers(String path,
                                                                       Project project) {
        if (exportedMembers.containsKey(path)) {
            return exportedMembers.get(path);
        } else {
            return calculateExportedMembers(path, project);
        }
    }

    /**
     * Returns a list with locations where there is a PsiFile that contains the member
     */
    public static List<String> getExportedMemberLocationsByName(String name) {
        return exportedMembersByName.get(name);
    }

    public static void removeFromIndex(String path) {
        exportedMembers.remove(path);
    }

    public static void clear() {
        exportedMembers.clear();
    }

    private static HashMap<String, List<PsiElement>> calculateExportedMembers(String path,
                                                                              Project project) {

        final OMTFile omtFile = ImportUtil.getOMTFile(path, project);
        if (omtFile == null) {
            return new HashMap<>();
        }
        final HashMap<String, List<PsiElement>> exportingMembersMap = omtFile.getExportingMembersMap();
        exportedMembers.put(path, exportingMembersMap);
//        exportingMembersMap.keySet().forEach(key -> exportedMembersByName.put(key, path));
        return exportingMembersMap;
    }

}
