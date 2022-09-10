package com.misset.opp.odt.inspection;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTCodeInspectionUnresolvableReferenceTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeInspectionUnresolvableReference.class));
    }

    @Test
    void testHasErrorForMissingVariableDeclaration() {
        configureByText("@LOG($variable);");
        inspection.assertHasError("Cannot resolve variable '$variable'");
    }

    @Test
    void testCreatesVariableDeclaration() {
        configureByText("@LOG($variable);");
        inspection.invokeQuickFixIntention("Declare variable");
        assertEquals("VAR $variable;\n" +
                "@LOG($variable);", getFile().getText());
    }

    @Test
    void testHasNoErrorForDeclaredVariableDeclaration() {
        configureByText("VAR $variable;\n" +
                "@LOG($variable);");
        inspection.assertNoError("Cannot resolve variable '$variable'");
    }

    @Test
    void testHasErrorForMissingCallDeclaration() {
        configureByText("@LAG($variable);");
        inspection.assertHasError("Cannot resolve call 'LAG'");
    }

    @Test
    void testHasNoErrorForExistingCallDeclaration() {
        configureByText("DEFINE COMMAND LAG => {} \n" +
                "@LAG($variable);");
        inspection.assertNoError("Cannot resolve call 'LAG'");
    }

    @Test
    void testHasErrorForMissingVariableReferenceInAnnotation() {
        String content = "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE COMMAND command => {} \n";
        configureByText(content);
        inspection.assertHasError("Cannot resolve parameter '$param'");
    }

    @Test
    void testHasErrorForMissingPrefixReferenceInAnnotation() {
        String content = "/**\n" +
                " * @param $param (abc:string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n";
        configureByText(content);
        inspection.assertHasError("Cannot resolve prefix 'abc'");
    }

    @Test
    void testNoErrorForKnownPrefixReferenceInAnnotation() {
        String content = "/**\n" +
                " * @param $param (xsd:string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n";
        configureByText(withPrefixes(content));
        inspection.assertNoError("Cannot resolve prefix 'xsd'");
    }

    @Test
    void testHasErrorForMissingOntologyReferenceInAnnotation() {
        String content = "/**\n" +
                " * @param $param (ont:ClassZ)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n";
        configureByText(withPrefixes(content));
        inspection.assertHasError("Cannot resolve type to ontology class / type");
    }

    @Test
    void testNoErrorForPrimitiveTypeAnnotation() {
        String content = "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n";
        configureByText(content);
        inspection.assertNoError("Cannot resolve type to ontology class / type");
    }

    @Test
    void testNoErrorForKnownOntologyReferenceInAnnotation() {
        String content = "/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE COMMAND command($param) => {} \n";
        configureByText(withPrefixes(content));
        inspection.assertNoError("Cannot resolve type to ontology class / type");
    }

}
