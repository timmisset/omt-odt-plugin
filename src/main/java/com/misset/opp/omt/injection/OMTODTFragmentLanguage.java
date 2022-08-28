package com.misset.opp.omt.injection;

import com.intellij.lang.Language;
import com.misset.opp.odt.ODTLanguage;

public class OMTODTFragmentLanguage extends Language {
    public static final OMTODTFragmentLanguage INSTANCE = new OMTODTFragmentLanguage();

    private OMTODTFragmentLanguage() {
        super(ODTLanguage.INSTANCE, "OMTODTFragment");
    }
}
