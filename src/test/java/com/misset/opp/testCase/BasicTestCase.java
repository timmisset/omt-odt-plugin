package com.misset.opp.testCase;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.misset.opp.omt.psi.OMTFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public abstract class BasicTestCase extends LightJavaCodeInsightFixtureTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
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
        return type.cast(psiFile);
    }

}
