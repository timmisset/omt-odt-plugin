package com.misset.opp.odt.inspection.quikfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTDefinePrefix;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import org.jetbrains.annotations.NotNull;

public class ODTRegisterPrefixLocalQuickFix implements LocalQuickFix {

    private String prefix;
    private String namespace;
    public ODTRegisterPrefixLocalQuickFix(String prefix, String namespace) {
        this.prefix = prefix;
        this.namespace = namespace;
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return "Register prefix";
    }

    @Override
    public @IntentionName @NotNull String getName() {
        return "Register prefix in ODT as: " + namespace;
    }

    @Override
    public void applyFix(@NotNull Project project,
                         @NotNull ProblemDescriptor descriptor) {
        addPrefix(project, descriptor.getPsiElement());
    }

    public void addPrefix(@NotNull Project project,
                          PsiElement psiElement) {
        if (psiElement == null || !psiElement.isValid()) {
            return;
        }

        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(psiElement, ODTScript.class);
        if (script == null) {
            return;
        }

        // retrieve as scriptline, which is what we need to insert
        final ODTScriptLine definePrefix = ODTElementGenerator.getInstance(project)
                .createDefinePrefix(prefix, namespace);
        if (definePrefix == null) {
            return;
        }
        // check if there is an anchor to use, an existing DEFINE prefix at the root script
        final ODTScriptLine existingDefinePrefix = PsiTreeUtil.findChildrenOfAnyType(script, ODTDefinePrefix.class).stream()
                .filter(existingPrefix -> PsiTreeUtil.getParentOfType(existingPrefix, ODTScript.class) == script)
                .map(existingPrefix -> PsiTreeUtil.getParentOfType(existingPrefix, ODTScriptLine.class))
                .findFirst()
                .orElse(null);

        if(existingDefinePrefix == null) {
            script.addBefore(definePrefix, script.getFirstChild());
        } else {
            script.addAfter(definePrefix, existingDefinePrefix);
        }
    }

}
