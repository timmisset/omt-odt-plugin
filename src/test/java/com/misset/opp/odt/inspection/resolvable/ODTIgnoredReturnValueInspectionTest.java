package com.misset.opp.odt.inspection.resolvable;

import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTIgnoredReturnValueInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTIgnoredReturnValueInspection.class));
    }

    @Test
    void testHasWarningWhenIgnoredQuery() {
        configureByText("true");
        inspection.assertHasWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningWhenODTStatement() {
        ODTFileTestImpl odtFileTest = configureByText("true");
        odtFileTest.setIsStatement(true);
        inspection.assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasWarningWhenNotVoid() {
        ODTFileTestImpl odtFileTest = configureByText("@command();");
        Callable callable = mock(Callable.class);
        doReturn("@command").when(callable).getCallId();
        doReturn(false).when(callable).isVoid();
        doReturn(Set.of(OppModelConstants.getXsdStringInstance())).when(callable).resolve(any(Context.class));
        odtFileTest.addCallable(callable);
        inspection.assertHasWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testNoWarningWhenVoid() {
        ODTFileTestImpl odtFileTest = configureByText("@command();");
        Callable callable = mock(Callable.class);
        doReturn("@command").when(callable).getCallId();
        doReturn(true).when(callable).isVoid();
        odtFileTest.addCallable(callable);
        inspection.assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningWhenCallableIsBuiltin() {
        ODTFileTestImpl odtFileTest = configureByText("@command();");
        Callable callable = mock(Callable.class);
        doReturn("@command").when(callable).getCallId();
        doReturn(false).when(callable).isVoid();
        doReturn(true).when(callable).isBuiltin();
        doReturn(Set.of(OppModelConstants.getXsdStringInstance())).when(callable).resolve(any(Context.class));
        odtFileTest.addCallable(callable);
        odtFileTest.setIsStatement(true);
        inspection.assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }
}
