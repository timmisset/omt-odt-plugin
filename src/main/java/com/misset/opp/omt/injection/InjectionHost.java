package com.misset.opp.omt.injection;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiLanguageInjectionHost;

import java.util.List;

public interface InjectionHost extends PsiLanguageInjectionHost {

    /**
     * The text-ranges within the host that are injectable with another language
     */
    List<TextRange> getTextRanges();

    /**
     * The prefix to be added to the injected content
     */
    String getPrefix();

    /**
     * The suffix to be added to the injected content
     */
    String getSuffix();

}
