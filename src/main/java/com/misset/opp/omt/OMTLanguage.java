package com.misset.opp.omt;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.impl.PsiModificationTrackerImpl;
import org.jetbrains.yaml.YAMLLanguage;

/**
 * The OMT Language is the extension on top of YAML and should be processed first as YAML
 * specific classes based on document positions will be created to make it more OMT specific
 */
public class OMTLanguage extends Language {
    public static final OMTLanguage INSTANCE = new OMTLanguage();
    public static ModificationTracker getLanguageModificationTracker(Project project) {
        return new PsiModificationTrackerImpl(project).forLanguage(
                OMTLanguage.INSTANCE);
    }
    protected OMTLanguage() {
        super(YAMLLanguage.INSTANCE, "OMT");
    }
}
