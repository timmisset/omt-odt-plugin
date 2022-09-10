package com.misset.opp.indexing;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Index that holds is run once to determine all existing prefix-namespace mappings in the OMT files
 * Used to suggest registering prefixes in OMT files when the prefix is unknown or a fully qualified uri is used
 */
public class PrefixIndex {

    private PrefixIndex() {
        // empty constructor
    }

    private static final HashMap<String, List<String>> map = new HashMap<>();

    public static List<String> getNamespaces(String prefix) {
        return map.getOrDefault(prefix, Collections.emptyList());
    }

    public static List<String> getPrefixes(String namespace) {
        return map.getOrDefault(namespace, Collections.emptyList());
    }

    public static void addToIndex(String key, String value) {
        map.computeIfAbsent(key, s -> new ArrayList<>()).add(value);
        map.computeIfAbsent(value, s -> new ArrayList<>()).add(key);
    }

    public static void orderIndexByFrequency() {
        // the list is ordered first, the more times a specific IRI is used for a prefix the higher it
        // moves up the list. Then the list is filtered (distinct)
        try {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                final List<String> list = entry.getValue();
                list.sort(Comparator.comparingInt(value -> Collections.frequency(list, value)).reversed());
                final List<String> orderedList = list.stream().distinct().collect(Collectors.toList());
                entry.setValue(orderedList);
            }
        } catch (IllegalArgumentException ignored) {
            // open issue: https://github.com/timmisset/omt-odt-plugin/issues/130
            // sometimes the ordering fails due to a TimSort contract violation. However, this is not reproducible in dev
            // mode and therefor is quite difficult to fix
        }

    }

    public static void clear() {
        map.clear();
    }
}
