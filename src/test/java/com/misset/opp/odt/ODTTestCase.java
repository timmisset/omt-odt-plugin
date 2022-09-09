package com.misset.opp.odt;

import com.intellij.psi.PsiFile;
import com.misset.opp.testCase.BasicTestCase;
import com.misset.opp.testCase.CompletionUtil;
import com.misset.opp.testCase.InspectionUtil;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public class ODTTestCase extends BasicTestCase<ODTFileTestImpl> {

    static {
        ODTParserDefinition.setFileViewProvider2ODTFile(ODTFileTestImpl::new);
    }

    public ODTTestCase() {
        super(ODTFileType.INSTANCE);
    }

    protected InspectionUtil inspection;
    protected CompletionUtil completion;

    @BeforeEach
    public void setUp() {
        super.setUp();
        setOntologyModel();
        inspection = new InspectionUtil(myFixture);
        completion = new CompletionUtil(myFixture);
    }

    @Override
    protected ODTFileTestImpl castToFile(PsiFile file) {
        return (ODTFileTestImpl) file;
    }

    protected String withPrefixes(String content) {
        String prefixes = testPrefixes.entrySet().stream()
                .map(entry -> String.format("PREFIX %s: <%s>;", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
        return prefixes + "\n" + content;
    }
}
