package com.misset.opp.omt.startup;

import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class IndexOMTPrefixes {

    private IndexOMTPrefixes() {

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
                .forEach(IndexOMTPrefixes::addToIndex);
    }

    private static void addToIndex(YAMLMapping mapping) {
        mapping.getKeyValues().forEach(IndexOMTPrefixes::addToIndex);
    }

    private static void addToIndex(YAMLKeyValue keyValue) {
        PrefixIndex.addToIndex(keyValue.getKeyText(), trimValue(keyValue.getValueText()));
        PrefixIndex.addToIndex(trimValue(keyValue.getValueText()), keyValue.getKeyText());
    }

    private static String trimValue(String valueText) {
        return valueText.substring(1, valueText.length() - 1);
    }
}
