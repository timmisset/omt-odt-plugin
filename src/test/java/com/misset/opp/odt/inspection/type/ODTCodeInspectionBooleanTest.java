package com.misset.opp.odt.inspection.type;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.model.util.OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN;

class ODTCodeInspectionBooleanTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeInspectionBoolean.class));
    }

    @Test
    void testHasErrorInIfBlock() {
        configureByText("IF 12 { @LOG('hi') }");
        inspection.assertHasError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInIfBlock() {
        configureByText("IF true { @LOG('hi') }");
        inspection.assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasErrorInBooleanStatement() {
        configureByText("12 AND true");
        inspection.assertHasError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInBooleanStatement() {
        configureByText("false AND true");
        inspection.assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInBooleanStatementWithEquation() {
        configureByText("false AND 1 == 1");
        inspection.assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

}
