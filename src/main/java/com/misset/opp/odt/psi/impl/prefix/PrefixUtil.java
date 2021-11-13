package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

public class PrefixUtil {

    public static String getFullyQualifiedUri(YAMLKeyValue prefix,
                                              String localName) {
        return Optional.of(prefix)
                .map(YAMLKeyValue::getValueText)
                .map(namespace -> createURI(namespace, localName))
                .orElse(null);
    }

    public static String getFullyQualifiedUri(ODTDefinePrefix definePrefix, String localName) {
        return Optional.of(definePrefix)
                .map(PsiElement::getLastChild)
                .map(PsiElement::getText)
                .map(namespace -> createURI(namespace, localName))
                .orElse(null);
    }

    private static String createURI(String namespace, String localName) {
        return Optional.of(namespace)
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(1, s.length() - 1) + localName)
                .orElse(null);
    }

}
