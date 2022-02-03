package com.misset.opp.omt.indexing;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Index that holds references to the PsiCallable elements that are exportable from OMT files.
 * Mapping the exportable PsiCallable elements of an OMT file is time-consuming because many can be part of the
 * injected language fragments. Therefore, this index will cache that information so it can be re-used when
 * nothing is changed within the files.
 * Resetting the index is triggered by the clear caches mechanism of the OMTFile
 */
public class OMTExportedMembersIndex {

    private static final HashMap<OMTFile, HashMap<String, List<PsiCallable>>> exportedMembers = new HashMap<>();
    private static final Logger LOGGER = Logger.getInstance(OMTExportedMembersIndex.class);

    public static HashMap<String, List<PsiCallable>> getExportedMembers(OMTFile file) {
        if (!file.isValid()) {
            return new HashMap<>();
        }
        return LoggerUtil.computeWithLogger(LOGGER, "Retrieving exporting members of " + file.getName(), () -> {
            if (exportedMembers.containsKey(file)) {
                return getCleanedExportedMembers(exportedMembers.get(file));
            } else {
                analyse(file);
                return exportedMembers.getOrDefault(file, new HashMap<>());
            }
        });
    }

    // Remove any non-valid PsiElements from the collection
    private static HashMap<String, List<PsiCallable>> getCleanedExportedMembers(HashMap<String, List<PsiCallable>> map) {
        for (String key : map.keySet()) {
            ArrayList<PsiCallable> psiCallables = new ArrayList<>(map.get(key));
            psiCallables.removeIf(psiCallable -> !psiCallable.isValid());
            map.put(key, Collections.unmodifiableList(psiCallables));
        }
        return map;
    }

    public static void removeFromIndex(OMTFile file) {
        exportedMembers.remove(file);
    }

    public static void analyse(OMTFile file) {
        LoggerUtil.runWithLogger(LOGGER, "Analysis of " + file.getName(), () -> {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(file.getProject());
            if (file.getVirtualFile() == null) {
                return;
            }
            statusBar.setInfo("Starting analysis of exportables for OMT File: " + file.getName());
            exportedMembers.put(file, file.getExportingMembersMap());
            statusBar.setInfo("Finished analysis of exportables for OMT File: " + file.getName()");
        });
    }

    public static void clear() {
        exportedMembers.clear();
    }

}
