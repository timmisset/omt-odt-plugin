package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTVariableAssignmentInspection.ERROR_READ_ONLY;
import static com.misset.opp.odt.inspection.ODTVariableAssignmentInspection.WARNING_NO_SECOND_ARGUMENT;

class ODTVariableAssignmentInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTVariableAssignmentInspection.class);
    }

    @Test
    void testErrorWhenWritingToGlobalVariable() {
        String content = insideProcedureRunWithPrefixes(
                "$username = 'newName';"
        );
        configureByText(content);
        assertHasError(ERROR_READ_ONLY);
    }

    @Test
    void testErrorWhenWritingToReadOnlyLocalVariable() {
        String content = insideActivityWithPrefixes(
                "variables: \n" +
                        "-  name: $variable\n" +
                        "   readonly: true\n" +
                        "onStart: |\n" +
                        "   $variable = 'newValue';\n"
        );
        configureByText(content);
        assertHasError(ERROR_READ_ONLY);
    }

    @Test
    void testWarningWhenAssigningNonExistingSecondArgumentNonCommand() {
        String content = "queries: |\n" +
                "   DEFINE QUERY query => 'hi';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => {" +
                "       VAR $variableA, $variableB = query;" +
                "   }";
        configureByText(content);
        assertHasWarning(WARNING_NO_SECOND_ARGUMENT);
    }

    @Test
    void testWarningWhenAssigningNonExistingSecondArgumentCommand() {
        String content =
                "commands: |\n" +
                        "   DEFINE COMMAND commandA => { }\n" +
                        "   DEFINE COMMAND commandB => {\n" +
                        "       VAR $variableA, $variableB = @commandA();\n" +
                        "   }\n";
        configureByText(content);
        assertHasWarning(WARNING_NO_SECOND_ARGUMENT);
    }

    @Test
    void testNoWarningWhenCallingActivity() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart:\n" +
                "           @LOG();\n" +
                "   AnotherActivity: !Activity\n" +
                "       onStart:\n" +
                "           VAR $variable, $committed = @Activity();\n" +
                "";
        configureByText(content);
        assertNoWarning(WARNING_NO_SECOND_ARGUMENT);
    }

    @Test
    void testNoWarningWhenCallingProcedure() {
        String content = "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           @LOG();\n" +
                "   AnotherActivity: !Activity\n" +
                "       onStart:\n" +
                "           VAR $variable, $committed = @Procedure();\n" +
                "";
        configureByText(content);
        assertNoWarning(WARNING_NO_SECOND_ARGUMENT);
    }
}