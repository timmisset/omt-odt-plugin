package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryParentheses.UNNECESSARY_PARENTHESES;

class ODTStyleInspectionUnnecessaryParenthesesTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTStyleInspectionUnnecessaryParentheses.class));
    }

    @Test
    void testShowsWarningWhenUnnecessaryEmptyOperatorCall() {
        configureByText("LOG();");
        inspection.assertHasWarning(UNNECESSARY_PARENTHESES);
    }

    @Test
    void testRemovesParentheses() {
        configureByText("LOG();");
        inspection.invokeQuickFixIntention("Remove parentheses");
        Assertions.assertFalse(getFile().getText().contains("LOG();"));
        Assertions.assertTrue(getFile().getText().contains("LOG;"));
    }

    @Test
    void testShowsNoWarningWhenOperatorCallWithArguments() {
        configureByText("LOG('test');");
        inspection.assertNoWarning(UNNECESSARY_PARENTHESES);
    }

    @Test
    void testShowsNoWarningWhenEmptyCommandCall() {
        configureByText("@LOG();");
        inspection.assertNoWarning(UNNECESSARY_PARENTHESES);
    }

    @Test
    void testShowsWarningForDefineQueryStatementWithoutArguments() {
        configureByText("DEFINE QUERY query() => '';");
        inspection.assertHasWarning(UNNECESSARY_PARENTHESES);
    }

    @Test
    void testRemovesParenthesesForDefineQueryStatement() {
        configureByText("DEFINE QUERY query() => '';");
        inspection.invokeQuickFixIntention("Remove parentheses");
        assertEquals("DEFINE QUERY query => '';", getFile().getText());
    }

    @Test
    void testShowsWarningForDefineCommandStatementWithoutArguments() {
        configureByText("DEFINE COMMAND command() => { }");
        inspection.assertHasWarning(UNNECESSARY_PARENTHESES);
    }

    @Test
    void testRemovesParenthesisForDefineCommandStatement() {
        configureByText("DEFINE COMMAND command() => { }");
        inspection.invokeQuickFixIntention("Remove parentheses");
        assertEquals("DEFINE COMMAND command => { }", getFile().getText());
    }
}
