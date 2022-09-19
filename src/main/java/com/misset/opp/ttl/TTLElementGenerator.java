package com.misset.opp.ttl;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.LocalTimeCounter;
import com.misset.opp.ttl.psi.TTLFile;
import com.misset.opp.ttl.psi.TTLIriReference;
import com.misset.opp.ttl.psi.TTLLocalname;
import org.jetbrains.annotations.NotNull;

@Service
public final class TTLElementGenerator {

    private final Project project;

    public TTLElementGenerator(Project project) {
        this.project = project;
    }

    public static TTLElementGenerator getInstance(@NotNull Project project) {
        return project.getService(TTLElementGenerator.class);
    }

    @NotNull
    private TTLFile createDummyOMTFileWithText(@NotNull String text) {
        return (TTLFile) PsiFileFactory.getInstance(project)
                .createFileFromText("temp." + TTLFileType.EXTENSION, TTLFileType.INSTANCE, text, LocalTimeCounter.currentTime(), true);
    }

    public <T extends PsiElement> T fromFile(String text, Class<T> classToReturn) {
        final TTLFile dummyOMTFileWithText = createDummyOMTFileWithText(text);
        return PsiTreeUtil.findChildOfType(dummyOMTFileWithText, classToReturn);
    }

    public TTLLocalname getLocalName(String localname) {
        String content = String.format("ont:%s a ont:%s.", localname, localname);
        return fromFile(content, TTLLocalname.class);
    }

    public TTLIriReference getIriReference(String iri) {
        String content = String.format("ont:Class a <%s>.", iri);
        return fromFile(content, TTLIriReference.class);
    }
}
