package com.misset.opp.omt.indexing;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.*;

/**
 * Index that holds the PsiCallable names and which files import them. This is used to reduce
 * the search scope when finding references to a PsiCallable element
 */
public class OMTImportedMembersIndex {

    private static final HashMap<String, List<OMTFile>> importedMembers = new HashMap<>();
    private static final Set<OMTFile> containedFiles = new HashSet<>();

    private static final Logger LOGGER = Logger.getInstance(OMTImportedMembersIndex.class);

    public static List<OMTFile> getImportingFiles(String callableName) {
        return importedMembers.getOrDefault(callableName, Collections.emptyList());
    }

    public static void removeFromIndex(OMTFile omtFile) {
        importedMembers.keySet().forEach(s -> removeFromIndex(s, omtFile));
    }

    private static void removeFromIndex(String key,
                                        OMTFile omtFile) {
        final List<OMTFile> omtFiles = new ArrayList<>();
        for (OMTFile file : getImportingFiles(key)) {
            if (file != omtFile) {
                omtFiles.add(file);
            }
        }
        importedMembers.put(key, Collections.unmodifiableList(omtFiles));
    }

    public static void clear() {
        importedMembers.clear();
        containedFiles.clear();
    }

    public static void analyse(OMTFile file) {
        LoggerUtil.runWithLogger(LOGGER, "Analysis of " + file.getName(), () -> {
            if (containedFiles.contains(file)) {
                removeFromIndex(file);
            }
            Optional.ofNullable(PsiTreeUtil.findChildOfType(file, YAMLMapping.class))
                    .map(yamlMapping -> yamlMapping.getKeyValueByKey("import"))
                    .map(importMap -> PsiTreeUtil.findChildrenOfType(importMap, YAMLSequenceItem.class))
                    .stream()
                    .flatMap(Collection::stream)
                    .map(YAMLSequenceItem::getValue)
                    .filter(Objects::nonNull)
                    .distinct()
                    .forEach(yamlValue -> addToIndex(yamlValue.getText(), file));
            containedFiles.add(file);
        });
    }

    private static void addToIndex(String name,
                                   OMTFile file) {
        final ArrayList<OMTFile> filesImportingByName = new ArrayList<>(importedMembers.getOrDefault(name,
                Collections.emptyList()));
        filesImportingByName.add(file);
        importedMembers.put(name, Collections.unmodifiableList(filesImportingByName));
    }

}
