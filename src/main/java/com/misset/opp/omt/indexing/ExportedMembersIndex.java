package com.misset.opp.omt.indexing;

import com.intellij.openapi.diagnostic.Logger;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExportedMembersIndex {

    private static final HashMap<OMTFile, HashMap<String, List<PsiCallable>>> exportedMembers = new HashMap<>();
    private static final HashMap<String, List<PsiCallable>> exportedMembersByName = new HashMap<>();
    private static final Logger LOGGER = Logger.getInstance(ImportedMembersIndex.class);

    public static HashMap<String, List<PsiCallable>> getExportedMembers(OMTFile file) {
        if (exportedMembers.containsKey(file)) {
            return exportedMembers.get(file);
        } else {
            analyse(file);
            return exportedMembers.getOrDefault(file, new HashMap<>());
        }
    }

    public static void removeFromIndex(OMTFile file) {
        exportedMembers.remove(file);
    }

    public static void analyse(OMTFile file) {
        LoggerUtil.runWithLogger(LOGGER, "Analysis of " + file.getName(), () -> {
            if (file.getVirtualFile() == null) {
                return;
            }
            exportedMembers.put(file, file.getExportingMembersMap());

            HashMap<String, List<PsiCallable>> declaredExportingMembersMap = file.getDeclaredExportingMembersMap();
            declaredExportingMembersMap.keySet().forEach(
                    s -> {
                        List<PsiCallable> paths = exportedMembersByName.getOrDefault(s, new ArrayList<>());
                        paths.addAll(declaredExportingMembersMap.get(s));
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

    public static void clear() {
        exportedMembers.clear();
        exportedMembersByName.clear();
    }

}
