package com.misset.opp.odt;

import com.intellij.lang.Language;

public class ODTLanguage extends Language {
    protected ODTLanguage() {
        super("ODT");
    }

    public static final ODTLanguage INSTANCE = new ODTLanguage();
}
