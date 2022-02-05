package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTCodeInspectionUnresolvableReferenceTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionUnresolvableReference.class);
    }

    @Test
    void testHasErrorForMissingVariableDeclaration() {
        String content = insideProcedureRunWithPrefixes("@LOG($variable);");
        configureByText(content);
        assertHasWarning("Cannot resolve variable '$variable'");
    }

    @Test
    void testCreatesVariableDeclaration() {
        String content = insideProcedureRunWithPrefixes("@LOG($variable);");
        OMTFile omtFile = configureByText(content);
        invokeQuickFixIntention("Declare variable");
        ReadAction.run(() -> Assertions.assertTrue(omtFile.getText().contains("VAR $variable;")));
    }

    @Test
    void testHasNoErrorForDeclaredVariableDeclaration() {
        String content = insideProcedureRunWithPrefixes("VAR $variable;\n" +
                "@LOG($variable);");
        configureByText(content);
        assertNoWarning("Cannot resolve variable '$variable'");
    }

    @Test
    void testHasErrorForMissingCallDeclaration() {
        String content = insideProcedureRunWithPrefixes("@LAG($variable);");
        configureByText(content);
        assertHasWarning("Cannot resolve call 'LAG'");
    }

    @Test
    void testHasNoErrorForExistingCallDeclaration() {
        String content = insideProcedureRunWithPrefixes("" +
                "DEFINE COMMAND LAG => {} \n" +
                "@LAG($variable);");
        configureByText(content);
        assertNoWarning("Cannot resolve call 'LAG'");
    }

}
