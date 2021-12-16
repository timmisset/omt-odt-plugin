package com.misset.opp.ttl;

import com.intellij.lang.Language;

public class TTLLanguage extends Language {
    public static final TTLLanguage INSTANCE = new TTLLanguage();

    private TTLLanguage() {
        super("Turtle");
    }
}
