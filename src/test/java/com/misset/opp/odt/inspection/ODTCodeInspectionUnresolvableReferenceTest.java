package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
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
        assertHasError("Cannot resolve variable '$variable'");
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
        assertNoError("Cannot resolve variable '$variable'");
    }

    @Test
    void testHasErrorForMissingCallDeclaration() {
        String content = insideProcedureRunWithPrefixes("@LAG($variable);");
        configureByText(content);
        assertHasError("Cannot resolve call 'LAG'");
    }

    @Test
    void testHasNoErrorForExistingCallDeclaration() {
        String content = insideProcedureRunWithPrefixes("" +
                "DEFINE COMMAND LAG => {} \n" +
                "@LAG($variable);");
        configureByText(content);
        assertNoError("Cannot resolve call 'LAG'");
    }

    @Test
    void testHasErrorForMissingVariableReferenceInAnnotation() {
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE COMMAND command => {} \n");
        configureByText(content);
        assertHasError("Cannot resolve parameter '$param'");
    }

    @Test
    void testHasErrorForMissingPrefixReferenceInAnnotation() {
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (abc:string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n");
        configureByText(content);
        assertHasError("Cannot resolve prefix 'abc'");
    }

    @Test
    void testNoErrorForKnownPrefixReferenceInAnnotation() {
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (xsd:string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n");
        configureByText(content);
        assertNoError("Cannot resolve prefix 'xsd'");
    }

    @Test
    void testHasErrorForMissingOntologyReferenceInAnnotation() {
        OMTOntologyTestCase.initOntologyModel();
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (ont:ClassZ)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n");
        configureByText(content);
        assertHasError("Cannot resolve type to ontology class / type");
    }

    @Test
    void testNoErrorForPrimitiveTypeAnnotation() {
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n");
        configureByText(content);
        assertNoError("Cannot resolve type to ontology class / type");
    }

    @Test
    void testNoErrorForKnownOntologyReferenceInAnnotation() {
        OMTOntologyTestCase.initOntologyModel();
        String content = insideProcedureRunWithPrefixes("" +
                "/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n");
        configureByText(content);
        assertNoError("Cannot resolve type to ontology class / type");
    }

}
