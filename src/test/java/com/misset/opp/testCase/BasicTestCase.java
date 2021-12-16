package com.misset.opp.testCase;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public abstract class BasicTestCase<T extends PsiFile> extends LightJavaCodeInsightFixtureTestCase {

    @BeforeEach
    protected void setUp() {
        try{
            super.setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    protected void tearDown() {
        try{
            super.tearDown();
            ExportedMembersIndex.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private FileType fileType;
    public BasicTestCase(FileType fileType) {
        this.fileType = fileType;
    }

    private static int files = 0;

    protected String getFileName() {
        return getFileName(fileType.getDefaultExtension());
    }

    protected String getFileName(String extension) {
        return String.format("test-%s.%s", files++, extension);
    }

    protected T configureByText(String content) {
        return configureByText(getFileName(), content, false);
    }

    protected T configureByText(String fileName,
                                String content) {
        return configureByText(fileName, content, false);
    }

    protected T configureByText(String content,
                                boolean acceptErrorElements) {
        return configureByText(getFileName(), content, acceptErrorElements);
    }

    protected abstract T castToFile(PsiFile file);

    protected void buildIndexes(T file) {
    }

    protected OMTFile getContainingOMTFile(ODTFileImpl file) {
        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(file.getProject());
        return (OMTFile) ReadAction.compute(() -> instance.getInjectionHost(file).getContainingFile());
    }

    protected T configureByText(String fileName,
                                String content,
                                boolean acceptErrorElements) {
        if (myFixture == null) {
            fail("Fixture is not defined, call super.setUp()");
        }
        final PsiFile psiFile = myFixture.configureByText(fileName, content);
        if (!acceptErrorElements && PsiTreeUtil.hasErrorElements(psiFile)) {
            String errorMessage = ReadAction.compute(() -> PsiTreeUtil.findChildrenOfType(psiFile,
                            PsiErrorElement.class)
                    .stream()
                    .map(psiErrorElement -> String.format("description: %s, error element: %s",
                            psiErrorElement.getErrorDescription(),
                            psiErrorElement.getText()))
                    .collect(Collectors.joining("\n")));
            fail(String.format("Configured PsiFile has an error element: %n%s%n%n%s", errorMessage, ReadAction.compute(
                    psiFile::getText)));
        }
        final T file = castToFile(psiFile);
        buildIndexes(file);
        return file;
    }

    protected void withProgress(Runnable runnable) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(
                runnable, "Test", false, getProject()
        );
    }

}
