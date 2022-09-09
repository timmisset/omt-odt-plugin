package com.misset.opp.odt.inspection.calls.commands;

import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.CallableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionForEach.ALL_VALUES_ARE_TREATED_EQUAL;
import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionForEach.CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTCommandInspectionForEachTest extends ODTTestCase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCommandInspectionForEach.class));
    }

    @Test
    void testForEachCommandCannotCallProcedureInsideForEachError() {
        String content = withPrefixes("" +
                "@FOREACH('', { @Procedure(); });");
        ODTFileTestImpl odtFileTest = configureByText(content);
        Callable callable = mock(Callable.class);
        doReturn("@Procedure").when(callable).getCallId();
        doReturn(CallableType.PROCEDURE).when(callable).getType();
        odtFileTest.addCallable(callable);
        inspection.assertHasError(CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH);
    }

    @Test
    void testForEachCommandCannotCallProcedureInsideForEachErrorNoErrors() {
        String content = withPrefixes("" +
                "@FOREACH('', { @LOG(''); });");
        configureByText(content);
        inspection.assertNoError(CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsWarning() {
        String content = withPrefixes("" +
                "@FOREACH('', { $value = ''; });");
        configureByText(content);
        inspection.assertHasWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsWarningWhenUpdatingCollection() {
        String content = withPrefixes("" +
                "VAR $someItem;\n" +
                "@FOREACH('', { $someItem / ont:someProperty += $value });");
        configureByText(content);
        inspection.assertHasWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenInsideLogicalBlock() {
        String content = withPrefixes("" +
                "@FOREACH('', { IF 1 == 2 { $value = ''; } });");
        configureByText(content);
        inspection.assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenInsideCall() {
        String content = withPrefixes("" +
                "DEFINE COMMAND SomeCommand() => {}\n" +
                "@FOREACH('', { @SomeCommand($value); });");
        configureByText(content);
        inspection.assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenUsedAsAssignmentValue() {
        String content = withPrefixes("" +
                "VAR $myValue;\n" +
                "@FOREACH(/ont:ClassA / ^rdf:type, { $myValue = $value; });");
        configureByText(content);
        inspection.assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningUsedAsAssigneeAndValue() {
        String content = withPrefixes("" +
                "VAR $myValue = /ont:ClassA / ^rdf:type;\n" +
                "@FOREACH($myValue, { $value / ont:propertyA = $value / ont:propertyB; });");
        configureByText(content);
        inspection.assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }
}
