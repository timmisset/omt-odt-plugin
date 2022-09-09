package com.misset.opp.odt.inspection.resolvable;

import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.resolvable.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTUnassignedVariableAssignmentInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTUnassignedVariableAssignmentInspection.class));
    }

    @Test
    void testHasWarningForUnassignedVariable() {
        String content = "VAR $variable;\n" +
                "@LOG($variable);";
        configureByText(content);
        inspection.assertHasWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForAssignedVariable() {
        String content = "VAR $variable = 'test';\n" +
                "@LOG($variable);";
        configureByText(content);
        inspection.assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningWhenGlobalVariable() {
        String content = "@LOG($username);";
        configureByText(content);
        inspection.assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForInputParameter() {
        String content = "DEFINE QUERY query($param) => $param;";
        configureByText(content);
        inspection.assertNoWarning("$param is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningWhenNotODTVariable() {
        String content = "@LOG($variable);";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Variable variable = mock(Variable.class);
        doReturn("$variable").when(variable).getName();
        odtFileTest.addVariable(variable);
        inspection.assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasWarningWhenODTVariable() {
        String content = "@LOG($variable);";
        ODTFileTestImpl odtFileTest = configureByText(content);
        Variable variable = mock(ODTVariable.class);
        doReturn("$variable").when(variable).getName();
        odtFileTest.addVariable(variable);
        inspection.assertHasWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForReAssignedVariable() {
        String content = "VAR $variable;\n" +
                "$variable = 'test';\n" +
                "@LOG($variable);";
        configureByText(content);
        inspection.assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasWarningForReAssignedVariable() {
        String content = "VAR $variable;\n" +
                "@LOG($variable);\n" +
                "$variable = 'test';\n";
        configureByText(content);
        inspection.assertHasWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForAssigningAttributes() {
        String content = "VAR $variable; $variable / <json:attribute> = 'myValue';\n";
        configureByText(content);
        inspection.assertNoWarning("$variable is used before it is assigned a value");
    }
}
