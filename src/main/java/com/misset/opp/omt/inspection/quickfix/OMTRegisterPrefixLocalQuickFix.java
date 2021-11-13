package com.misset.opp.omt.inspection.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTRegisterPrefixLocalQuickFix implements LocalQuickFix {

    private String prefix;
    private String iri;
    public OMTRegisterPrefixLocalQuickFix(String prefix, String iri) {
        this.prefix = prefix;
        this.iri = iri;
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return "Register prefix";
    }

    @Override
    public @IntentionName @NotNull String getName() {
        return "Register prefix in OMT as " + iri;
    }

    @Override
    public void applyFix(@NotNull Project project,
                         @NotNull ProblemDescriptor descriptor) {

        final PsiElement sourceYamlElement = getSourceYamlElement(descriptor);
        if(sourceYamlElement == null) { return; }
        final YAMLMapping rootMapping = PsiTreeUtil.getTopmostParentOfType(sourceYamlElement,
                YAMLMapping.class);
        if(rootMapping == null) { return; }
        final YAMLKeyValue prefix = createPrefix(project);
        final YAMLKeyValue prefixes = rootMapping.getKeyValueByKey("prefixes");
        if(prefixes != null) {
            final YAMLValue value = prefixes.getValue();
            if(value instanceof YAMLMapping) {
                ((YAMLMapping)value).putKeyValue(prefix);
            }
        } else {
            rootMapping.putKeyValue(createPrefixes(project, prefix));
        }
    }
    private PsiElement getSourceYamlElement(@NotNull ProblemDescriptor descriptor) {
        final PsiElement psiElement = descriptor.getPsiElement();
        if(psiElement instanceof YAMLPsiElement || psiElement == null) {
            return psiElement;
        } else {
            final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(psiElement.getProject());
            return instance.getInjectionHost(psiElement);
        }
    }
    private YAMLKeyValue createPrefixes(Project project, YAMLKeyValue prefix) {
        return YAMLElementGenerator.getInstance(project)
                .createYamlKeyValue("prefixes", prefix.getText());
    }
    private YAMLKeyValue createPrefix(Project project) {
        return YAMLElementGenerator.getInstance(project)
                .createYamlKeyValue(prefix, "<" + iri + ">");
    }
}
