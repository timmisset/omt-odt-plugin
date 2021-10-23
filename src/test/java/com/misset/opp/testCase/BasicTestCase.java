package com.misset.opp.testCase;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.omt.psi.OMTFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public abstract class BasicTestCase extends LightJavaCodeInsightFixtureTestCase {

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


    protected OMTFile configureByText(String content) {
        return configureByText(getFileName(), content, false);
    }

    protected OMTFile configureByText(String fileName, String content) {
        return configureByText(fileName, content, false);
    }

    protected OMTFile configureByText(String content, boolean acceptErrorElements) {
        return configureByText(getFileName(), content, acceptErrorElements);
    }
    protected OMTFile configureByText(String fileName,
                                                    String content,
                                                    boolean acceptErrorElements) {
        return configureByText(fileName, content, acceptErrorElements, OMTFile.class);
    }
    protected <T extends PsiFile> T configureByText(String fileName,
                                                    String content,
                                                    boolean acceptErrorElements,
                                                    Class<T> type) {
        if (myFixture == null) {
            fail("Fixture is not defined, call super.setUp()");
        }
        final PsiFile psiFile = myFixture.configureByText(fileName, content);
        if (!acceptErrorElements && PsiTreeUtil.hasErrorElements(psiFile)) {
            String errorMessage = ReadAction.compute(() -> PsiTreeUtil.findChildrenOfType(psiFile, PsiErrorElement.class)
                    .stream()
                    .map(psiErrorElement -> String.format("description: %s, error element: %s", psiErrorElement.getErrorDescription(), psiErrorElement.getText()))
                    .collect(Collectors.joining("\n")));
            fail(String.format("Configured PsiFile has an error element: %n%s%n%n%s", errorMessage, ReadAction.compute(
                    psiFile::getText)));
        }
        if(psiFile instanceof ODTFileImpl) {
            // the fixture returns the nested ODT file instead of the containing OMT file
            return type.cast(getContainingOMTFile((ODTFileImpl) psiFile));
        }
        return type.cast(psiFile);
    }
    protected PsiFile getContainingOMTFile(ODTFileImpl file) {
        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(file.getProject());
        return ReadAction.compute(() -> instance.getInjectionHost(file).getContainingFile());
    }


}
