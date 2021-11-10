package com.misset.opp.odt.psi.impl.prefix;

import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public class PrefixUtil {

    public static String getFullyQualifiedUri(YAMLKeyValue prefix,
                                              String localName) {
        return Optional.of(prefix)
                .map(YAMLKeyValue::getValueText)
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(1, s.length() - 1) + localName)
                .orElse(null);
    }

}
