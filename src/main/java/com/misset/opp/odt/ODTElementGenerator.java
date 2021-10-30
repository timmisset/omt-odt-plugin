package com.misset.opp.odt;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.LocalTimeCounter;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import org.jetbrains.annotations.NotNull;

@Service
public final class ODTElementGenerator {

    private Project project;
    public ODTElementGenerator(Project project) {
        this.project = project;
    }

    public static ODTElementGenerator getInstance(@NotNull Project project) {
        return project.getService(ODTElementGenerator.class);
    }

    @NotNull
    public ODTFile createDummyOMTFileWithText(@NotNull String text) {
        return (ODTFile) PsiFileFactory.getInstance(project)
                .createFileFromText("temp." + ODTFileType.INSTANCE.getDefaultExtension(), ODTFileType.INSTANCE, text, LocalTimeCounter.currentTime(), true);
    }

    public ODTNamespacePrefix createNamespacePrefix(String prefix) {
        final ODTFile dummyOMTFileWithText = createDummyOMTFileWithText(prefix + ":dummy");
        return PsiTreeUtil.findChildOfType(dummyOMTFileWithText, ODTNamespacePrefix.class);
    }

}
