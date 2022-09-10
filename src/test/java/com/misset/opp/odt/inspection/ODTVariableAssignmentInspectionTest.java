package com.misset.opp.odt.inspection;

import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTVariableAssignmentInspection.ERROR_READ_ONLY;
import static com.misset.opp.odt.inspection.ODTVariableAssignmentInspection.WARNING_NO_SECOND_ARGUMENT;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTVariableAssignmentInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTVariableAssignmentInspection.class));
    }

    @Test
    void testErrorWhenWritingToGlobalVariable() {
        String content = "$variable = 'newValue';";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Variable variable = mock(Variable.class);
        doReturn("$variable").when(variable).getName();
        doReturn(true).when(variable).isGlobal();
        odtFileTest.addVariable(variable);
        inspection.assertHasError(ERROR_READ_ONLY);
    }

    @Test
    void testErrorWhenWritingToReadOnlyVariable() {
        String content = "$variable = 'newValue';";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Variable variable = mock(Variable.class);
        doReturn("$variable").when(variable).getName();
        doReturn(true).when(variable).isReadonly();
        odtFileTest.addVariable(variable);
        inspection.assertHasError(ERROR_READ_ONLY);
    }

    @Test
    void testWarningWhenAssigningNonExistingSecondArgumentNonCommand() {
        String content = "VAR $variableA, $variableB = @callable";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Callable callable = mock(Callable.class);
        doReturn("@callable").when(callable).getCallId();
        doReturn(Collections.emptySet()).when(callable).getSecondReturnArgument();
        odtFileTest.addCallable(callable);
        inspection.assertHasWarning(WARNING_NO_SECOND_ARGUMENT);
    }

    @Test
    void testNoWarningWhenSecondArgumentExists() {
        String content = "VAR $variableA, $variableB = @callable";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Callable callable = mock(Callable.class);
        doReturn("@callable").when(callable).getCallId();
        doReturn(Collections.singleton(OppModelConstants.getXsdStringInstance())).when(callable).getSecondReturnArgument();
        odtFileTest.addCallable(callable);
        inspection.assertNoWarning(WARNING_NO_SECOND_ARGUMENT);
    }
}
