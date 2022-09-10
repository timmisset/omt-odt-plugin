package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTCodeInspectionDefinedDuplicationTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeInspectionDefinedDuplication.class));
    }

    @Test
    void testHasDuplicationWarningInFile() {
        String content = "DEFINE QUERY query => 'a';\n" +
                "DEFINE QUERY query => 'b';";
        configureByText(content);
        inspection.assertHasWarning(ODTCodeInspectionDefinedDuplication.WARNING_MESSAGE_DUPLICATION);
    }

    @Test
    void testHasNoDuplicationWarningOnDifferentNamesInFile() {
        String content = "DEFINE QUERY queryA => 'a';\n" +
                "DEFINE QUERY queryB => 'b';";
        configureByText(content);
        inspection.assertNoWarning(ODTCodeInspectionDefinedDuplication.WARNING_MESSAGE_DUPLICATION);
    }

    @Test
    void testHasDuplicationWarningWhenAlsoAvailableExternally() {
        String content = "DEFINE COMMAND sameName => { }";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Callable callable = mock(Callable.class);
        doReturn("@sameName").when(callable).getCallId();
        odtFileTest.addCallable(callable);
        inspection.assertHasWarning(ODTCodeInspectionDefinedDuplication.WARNING_MESSAGE_DUPLICATION);
    }

}
