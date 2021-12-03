package com.misset.opp.omt.indexing;

import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ImportedMembersIndex {

    private static final HashMap<String, List<OMTFile>> importedMembers = new HashMap<>();
    private static final Set<OMTFile> containedFiles = new HashSet<>();

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
    }

    private static void addToIndex(String name,
                                   OMTFile file) {
        final ArrayList<OMTFile> filesImportingByName = new ArrayList<>(importedMembers.getOrDefault(name,
                Collections.emptyList()));
        filesImportingByName.add(file);
        importedMembers.put(name, Collections.unmodifiableList(filesImportingByName));
    }

}
