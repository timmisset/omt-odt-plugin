package com.misset.opp.omt.indexing;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.stream.Collectors;

public class OMTPrefixIndex {

    private static HashMap<String, List<String>> map = new HashMap<>();

    public static List<String> getNamespaces(String prefix) {
        return map.getOrDefault(prefix, Collections.emptyList());
    }

    public static List<String> getPrefixes(String namespace) {
        return map.getOrDefault(namespace, Collections.emptyList());
    }

    public static Task.Backgroundable getIndexTask(Project project) {
        return new Task.Backgroundable(project, "Indexing used prefixes") {
            @Override
            public void run(@Nullable ProgressIndicator indicator) {
                map = new HashMap<>();
                final PsiManager psiManager = PsiManager.getInstance(project);

                ReadAction.run(() -> FilenameIndex.getAllFilesByExt(project, "omt")
                        .stream()
                        .map(psiManager::findFile)
                        .filter(OMTFile.class::isInstance)
                        .map(OMTFile.class::cast)
                        .forEach(this::indexPrefixes));
                this.orderIndexByFrequency();
            }

            private void indexPrefixes(OMTFile file) {
                PsiTreeUtil.findChildrenOfType(file, YAMLKeyValue.class)
                        .stream()
                        .filter(keyValue -> keyValue.getKeyText().equals("prefixes"))
                        .map(YAMLKeyValue::getValue)
                        .filter(YAMLMapping.class::isInstance)
                        .map(YAMLMapping.class::cast)
                        .forEach(this::addToIndex);
            }

            private void addToIndex(YAMLMapping mapping) {
                mapping.getKeyValues().stream().forEach(this::addToIndex);
            }

            private void addToIndex(YAMLKeyValue keyValue) {
                addToIndex(keyValue.getKeyText(), trimValue(keyValue.getValueText()));
                addToIndex(trimValue(keyValue.getValueText()), keyValue.getKeyText());
            }

            private void addToIndex(String key, String value) {
                List<String> valuesForKey = map.getOrDefault(key, new ArrayList<>());
                valuesForKey.add(value);
                map.put(key, valuesForKey);
            }

            private String trimValue(String valueText) {
                return valueText.substring(1, valueText.length() - 1);
            }

            private void orderIndexByFrequency() {
                // the list is ordered first, the more times a specific IRI is used for a prefix the higher it
                // moves up the list. Then the list is filtered (distinct)
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    final List<String> list = entry.getValue();
                    list.sort(Comparator.comparing(i -> Collections.frequency(list, i)).reversed());
                    final List<String> orderedList = list.stream().distinct().collect(Collectors.toList());
                    entry.setValue(orderedList);
                }
            }
        };
    }

}
