package com.misset.opp.omt.indexing;

import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Index that holds is run once to determine all existing prefix-namespace mappings in the OMT files
 * Used to suggest registering prefixes in OMT files when the prefix is unknown or a fully qualified uri is used
 */
public class OMTPrefixIndex {

    private static final HashMap<String, List<String>> map = new HashMap<>();

    public static List<String> getNamespaces(String prefix) {
        return map.getOrDefault(prefix, Collections.emptyList());
    }

    public static List<String> getPrefixes(String namespace) {
        return map.getOrDefault(namespace, Collections.emptyList());
    }

    /**
     * Analyse and index all prefixes in the OMT file
     * Call orderIndexByFrequency() when all files are processed to sort the index by frequency
     */
    public static void analyse(OMTFile file) {
        if (!file.isValid()) {
            return;
        }
        PsiTreeUtil.findChildrenOfType(file, YAMLKeyValue.class)
                .stream()
                .filter(keyValue -> keyValue.getKeyText().equals("prefixes"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .forEach(OMTPrefixIndex::addToIndex);
    }

    private static void addToIndex(YAMLMapping mapping) {
        mapping.getKeyValues().forEach(OMTPrefixIndex::addToIndex);
    }

    private static void addToIndex(YAMLKeyValue keyValue) {
        addToIndex(keyValue.getKeyText(), trimValue(keyValue.getValueText()));
        addToIndex(trimValue(keyValue.getValueText()), keyValue.getKeyText());
    }

    private static void addToIndex(String key, String value) {
        List<String> valuesForKey = map.getOrDefault(key, new ArrayList<>());
        valuesForKey.add(value);
        map.put(key, valuesForKey);
    }

    private static String trimValue(String valueText) {
        return valueText.substring(1, valueText.length() - 1);
    }

    public static void orderIndexByFrequency() {
        // the list is ordered first, the more times a specific IRI is used for a prefix the higher it
        // moves up the list. Then the list is filtered (distinct)
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            final List<String> list = entry.getValue();
            list.sort(Comparator.comparing(i -> Collections.frequency(list, i)).reversed());
            final List<String> orderedList = list.stream().distinct().collect(Collectors.toList());
            entry.setValue(orderedList);
        }
    }

    public static void clear() {
        map.clear();
    }
}
