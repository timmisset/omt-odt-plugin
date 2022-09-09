package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTCodeInspectionUnreachable.UNREACHABLE_CODE;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTCodeInspectionUnreachableTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeInspectionUnreachable.class));
    }

    @Test
    void testHasWarningOnUnreachableCodeReturn() {
        configureByText("RETURN 1; @LOG('test');");
        inspection.assertHasWarning(UNREACHABLE_CODE);
    }

    @Test
    void testHasWarningOnUnreachableCodeAfterFinalCommand() {
        ODTFileTestImpl odtFileTest = configureByText("@FINAL(); @LOG('test');");
        Callable callable = mock(Callable.class);
        doReturn("@FINAL").when(callable).getCallId();
        doReturn(true).when(callable).isFinalCommand();
        odtFileTest.addCallable(callable);
        inspection.assertHasWarning(UNREACHABLE_CODE);
    }

    @Test
    void testHasNoWarningOnUnreachableCodeReturn() {
        configureByText("@LOG('test'); RETURN 1;");
        inspection.assertNoWarning(UNREACHABLE_CODE);
    }
}
