package com.misset.opp.odt.settings;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.misset.opp.odt.ODTLanguage;
import org.jetbrains.annotations.Nullable;

public class ODTCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public @Nullable CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
        return new ODTCustomCodeStyleSettings(settings);
    }

    private static class ODTCustomCodeStyleSettings extends CustomCodeStyleSettings {
        protected ODTCustomCodeStyleSettings(CodeStyleSettings container) {
            super(ODTLanguage.INSTANCE.getID(), container);
        }
    }
}
