package com.misset.opp.odt;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.LocalTimeCounter;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.call.ODTBaseCall;
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
    public <T extends PsiElement> T fromFile(String text, Class<T> classToReturn) {
        final ODTFile dummyOMTFileWithText = createDummyOMTFileWithText(text);
        return PsiTreeUtil.findChildOfType(dummyOMTFileWithText, classToReturn);
    }

    public ODTNamespacePrefix createNamespacePrefix(String prefix) {
        return fromFile(prefix + ":dummy", ODTNamespacePrefix.class);
    }

    public ODTBaseCall createCall(String name) {
        return fromFile("@" + name, ODTBaseCall.class);
    }

    public ODTVariable createVariable(String name) {
        return fromFile(name, ODTVariable.class);
    }
    public PsiComment createJavadocs(String comment) {
        return fromFile(comment, PsiComment.class);
    }

}
