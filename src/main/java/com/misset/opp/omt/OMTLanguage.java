package com.misset.opp.omt;

import com.intellij.lang.Language;
import org.jetbrains.yaml.YAMLLanguage;

/**
 * The OMT Language is the extension on top of YAML and should be processed first as YAML
 * specific classes based on document positions will be created to make it more OMT specific
 */
public class OMTLanguage extends Language {
    public static final OMTLanguage INSTANCE = new OMTLanguage();
    protected OMTLanguage() {
        super(YAMLLanguage.INSTANCE, "OMT");
    }
}
