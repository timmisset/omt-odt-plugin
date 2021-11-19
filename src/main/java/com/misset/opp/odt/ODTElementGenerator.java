package com.misset.opp.odt;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.LocalTimeCounter;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public final class ODTElementGenerator {

    private final Project project;
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

    public ODTScriptLine createDefinePrefix(String prefix,
                                            String namespace) {
        return fromFile("PREFIX " + prefix + ": <" + namespace + ">", ODTScriptLine.class);
    }

    public ODTCallName createCallName(String name) {
        return fromFile("@" + name, ODTCallName.class);
    }

    public ODTCall createCall(String name,
                              @Nullable String flag,
                              String... arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        if (flag != null) {
            stringBuilder.append("!").append(flag);
        }
        final String argumentContent = Arrays.stream(arguments)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.joining(", "));
        if (!argumentContent.isBlank()) {
            stringBuilder.append("(").append(argumentContent).append(")");
        }
        return fromFile(stringBuilder.toString(), ODTCall.class);
    }

    public ODTVariable createVariable(String name) {
        return fromFile(name, ODTVariable.class);
    }
}
